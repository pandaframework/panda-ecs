package org.pandaframework.ecs.entity.pool

import org.pandaframework.ecs.aspect.Aspect
import org.pandaframework.ecs.component.Component
import org.pandaframework.ecs.component.ComponentBits
import org.pandaframework.ecs.component.ComponentManager
import org.pandaframework.ecs.entity.Entity
import org.pandaframework.ecs.entity.EntitySubscription
import org.pandaframework.ecs.entity.EntitySubscriptionListener
import org.pandaframework.ecs.util.Bits
import org.pandaframework.ecs.util.identity.IdentityFactories
import org.pandaframework.ecs.util.pool.BasicObjectPool
import org.pandaframework.ecs.util.pool.ObjectPool
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

/**
 * @author Ranie Jade Ramiso
 */
class BasicEntityPool(private val componentManager: ComponentManager): EntityPool {
    private val identityFactory = IdentityFactories.recycling()

    // contains the ComponentBits of all entities
    // we use the identity as an index, it will always be greater than 0
    // recycling of unused indexes works out of the box
    // this will be increased by BUFFER_GROW whenever we ran out of space
    // ComponentBits is implemented as a fly-weight, so it should be memory efficient
    // most of the time, elements of this buffer will point to the same instance
    private var entities = Array<ComponentBits?>(BUFFER_INITIAL, { null })
    private var entityEditors = Array<EntityEditor?>(BUFFER_INITIAL, { null })

    // this will be used to keep track the right most indexed that has a value
    // whenever a large number of entities are destroyed we need to shrink entities
    // we will use this index in the computation
    // shrinking will always leave an allowance (maybe by BUFFER_GROW) to lessen
    // the amount of re-allocation
    // whenever the entity with the highest identity is being destroyed, we just decrement this by 1
    // main disadvantage of this is when entities being destroyed are all in the middle
    private var highestIdentity = 0

    // we use this as an index for faster queries
    // keys are the ComponentBits, while the values are the entities
    // values dynamically grow and shrink just like entities, but shrinking is much simple here.
    // we just reduce the size of the array using bucketSizes (w/c keeps track the number of entities per bucket)
    // shrinking will also leave an allowance, just like entities
    private val buckets = HashMap<ComponentBits, IntArray>()
    private val bucketSizes = HashMap<ComponentBits, Int>()

    private val pools = mutableMapOf<KClass<out Component>, ObjectPool<Any>>()

    private val subscriptions = HashMap<Aspect, EntitySubscriptionImpl>()

    // with the setup above
    // create, edit and destroy operations can happen in constant time
    // except for cases were we need to grow and/or shrink the internal buffers.

    override fun create(): Entity {
        return identityFactory.generate().apply {
            trackEntity(this)
        }
    }

    override fun edit(entity: Entity): EntityEditor {
        val index = entity - 1
        require(index < entities.size, { "Entity '$entity' does not exist"})
        requireNotNull(entities[index], { "Entity '$entity' does not exist"})

        if (entityEditors[index] == null) {
            entityEditors[index] = object: EntityEditor {
                private val componentInstances = mutableMapOf<KClass<out Component>, Component>()

                override val entity: Entity
                    get() = entity

                override fun <T: Component> get(component: KClass<T>): T {

                    return if (!contains(component)) {
                        val id = componentManager.getId(component)
                        var bits = entities[index]!!
                        removeFromBucket(bits, entity)

                        // set the corresponding bit and update state
                        bits = bits.set(id)
                        entities[index] = bits
                        placeInBucket(bits, entity)

                        getPool(component).acquire().apply {
                            componentInstances.put(component, this)
                        }

                    } else {
                        return componentInstances[component] as T
                    }
                }

                private fun <T: Component> getPool(component: KClass<T>): ObjectPool<T> {
                    return pools.getOrPut(
                        component, { BasicObjectPool(100, { component.primaryConstructor!!.callBy(emptyMap()) }) }
                    ) as ObjectPool<T>
                }

                override fun <T: Component> contains(component: KClass<T>): Boolean {
                    val id  = componentManager.getId(component)
                    return entities[index]!![id]
                }

                override fun <T: Component> remove(component: KClass<T>) {
                    if (contains(component)) {
                        val id = componentManager.getId(component)
                        var bits = entities[index]!!
                        removeFromBucket(bits, entity)
                        val instance = componentInstances.remove(component)
                        getPool(component).release(instance as T)

                        // unset corresponding bit and update state
                        bits = bits.set(id, false)
                        entities[index] = bits
                        placeInBucket(bits, entity)
                    }
                }

                override fun cleanup() {
                    componentInstances.entries.forEach {
                        pools[it.key]!!.release(it.value)
                    }
                }
            }
        }

        return entityEditors[index]!!
    }

    override fun destroy(entity: Entity) {
        val index = entity - 1
        require(index < entities.size, { "Entity '$entity' does not exist"})
        requireNotNull(entities[index], { "Entity '$entity' does not exist"})
        removeFromBucket(entities[index]!!, entity)
        entities[index] = null
        entityEditors[index]!!.cleanup()
        entityEditors[index] = null
        identityFactory.free(entity)
    }

    override fun subscribe(aspect: Aspect): EntitySubscription {
        return subscriptions.getOrPut(aspect) { EntitySubscriptionImpl(aspect)}
    }

    private fun trackEntity(entity: Entity) {
        val componentBits = Bits.ZERO
        val index = entity - 1

        entities = if (entity > entities.size) {
            // resize entity editors buffer
            entityEditors = entityEditors.copyOf(entityEditors.size + BUFFER_GROW)
            entities.copyOf(entities.size + BUFFER_GROW)
        } else {
            entities
        }


        if (entity > highestIdentity) {
            highestIdentity = entity
        }

        entities[index] = componentBits
        placeInBucket(componentBits, entity)
    }

    private fun placeInBucket(componentBits: ComponentBits, entity: Entity) {
        var bucket = buckets.getOrElse(componentBits) { EMPTY_INT_ARRAY.copyOf(BUFFER_INITIAL)}
        var size = bucketSizes.getOrDefault(componentBits, 0)

        bucket = if (size + 1 > bucket.size) {
            bucket.copyOf(bucket.size + BUFFER_GROW)
        } else {
            bucket
        }
        // entity does not exist at this point, index is insertion index
        val index = if (size == 0) {
            // optimization
            0
        } else {
            Math.abs(bucket.binarySearch(entity, 0, size) + 1)
        }
        // shift elements to the right starting from index until the index of the last entity (size - 1)
        bucket.shiftRight(index, size - 1)

        bucket[index] = entity
        size += 1
        buckets.put(componentBits, bucket)
        bucketSizes.put(componentBits, size)

        notifyListeners(componentBits) {
            it.added(entity)
        }
    }

    private fun removeFromBucket(componentBits: ComponentBits, entity: Entity) {
        val bucket = buckets[componentBits]!!
        var size = bucketSizes[componentBits]!!

        // entity exists, index is the actual index of the entity
        val index = bucket.binarySearch(entity, 0, size)
        // shift everything to the left starting from index + 1 to size
        // index points to the entity we want to remove
        // since we moved the last entity, its previous index still contains a reference to it. That's
        // why we use size instead of size - 1
        // we don't shift if the removed item is the last item in the bucket
        val start = index + 1
        if (start < size) {
            bucket.shiftLeft(start, size)
        }
        size -= 1
        buckets.put(componentBits, bucket)
        bucketSizes.put(componentBits, size)

        notifyListeners(componentBits) {
            it.removed(entity)
        }
    }

    private inline fun notifyListeners(componentBits: ComponentBits,
                                       crossinline notify: (EntitySubscriptionListener) -> Unit) {
        subscriptions.filter { it.key.match(componentBits) }
            .forEach { _, subscription -> subscription.listeners.forEach { notify(it) } }
    }

    /**
     * @param start start index (inclusive)
     * @param end end index (inclusive)
     */
    private fun IntArray.shiftRight(start: Int, end: Int) {
        val lastIndex = end + 1
        require(start >= 0, { "$start should be greater than or equal to 0"})
        require(lastIndex < size)

        for (i in lastIndex downTo (start + 1)) {
            this[i] = this[i - 1]
        }
    }

    /**
     * @param start start index (inclusive)
     * @param end end index (inclusive)
     */
    private fun IntArray.shiftLeft(start: Int, end: Int) {
        val startIndex = start - 1
        require(startIndex >= 0)
        require(end <= size, { "$start - $end" })

        for (i in startIndex..(end - 1)) {
            this[i] = this[i + 1]
        }
    }

    private fun getEntities(aspect: Aspect): IntArray {

        val filtered = buckets.filter { (componentBits, _) ->
            aspect.match(componentBits)
        }

        return if (filtered.isEmpty()) {
            EMPTY_INT_ARRAY
        } else {
            filtered.entries.fold(EMPTY_INT_ARRAY) { base, entry ->
                val bucketSize = bucketSizes[entry.key]!!
                base.copyOf(base.size + bucketSize).apply {
                    System.arraycopy(entry.value, 0, this, base.size, bucketSize)
                }
            }
        }

    }

    private inner class EntitySubscriptionImpl(val aspect: Aspect): EntitySubscription {
        val listeners = mutableListOf<EntitySubscriptionListener>()

        override fun addListener(listener: EntitySubscriptionListener) {
            listeners.add(listener)
        }

        override fun removeListener(listener: EntitySubscriptionListener) {
            listeners.remove(listener)
        }

        override fun entities() = getEntities(aspect)
    }

    companion object {
        val EMPTY_INT_ARRAY = intArrayOf()

        val BUFFER_INITIAL = 100
        val BUFFER_GROW = 50
    }
}
