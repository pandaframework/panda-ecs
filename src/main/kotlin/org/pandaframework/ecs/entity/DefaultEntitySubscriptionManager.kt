package org.pandaframework.ecs.entity

import org.pandaframework.ecs.Mapper
import org.pandaframework.ecs.component.Component
import org.pandaframework.ecs.component.ComponentFactories
import org.pandaframework.ecs.component.ComponentIdentityManager
import org.pandaframework.ecs.util.Bag
import org.pandaframework.ecs.util.Bits
import org.pandaframework.ecs.util.identity.IdentityFactories
import java.util.HashMap
import java.util.LinkedList
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
internal class DefaultEntitySubscriptionManager(private val componentIdentityManager: ComponentIdentityManager,
                                       private val componentFactories: ComponentFactories)
    : EntitySubscriptionManager {
    private val identityFactory = IdentityFactories.recycling()

    private val entities = Bag<Bits>()

    private val components = Bag<HashMap<KClass<out Component>, Component>>()
    private val editors = Bag<EntityEditor>()
    private val aspects = HashMap<AspectImpl, Bits>()
    private val listeners = HashMap<AspectImpl, MutableList<EntitySubscription.Listener>>()

    private val subscriptionCache = HashMap<AspectImpl, EntitySubscription>()

    override fun subscribe(aspect: AspectImpl): EntitySubscription {
        return subscriptionCache.computeIfAbsent(aspect, {
            aspects.computeIfAbsent(aspect, {
                Bits()
            })
            val listeners = getListeners(aspect)

            object: EntitySubscription {
                override fun addListener(listener: EntitySubscription.Listener) {
                    listeners.add(listener)
                }

                override fun removeListener(listener: EntitySubscription.Listener) {
                    listeners.remove(listener)
                }

                override fun entities(): IntArray {
                    val entities = aspects[aspect]!!.setBits()
                    return IntArray(entities.size).apply {
                        entities.forEachIndexed { index, it ->
                            this[index] = it
                        }
                    }
                }
            }
        })
    }

    override fun create(): Int {
        val entity = identityFactory.generate()
        entities[entity] = Bits()
        return entity
    }

    override fun remove(entity: Int) {
        assertEntityAlive(entity)
        update(entity, null)
    }

    override fun edit(entity: Int): EntityEditor {
        assertEntityAlive(entity)
        return getEntityEditor(entity)
    }

    override fun <T: Component> mapper(component: KClass<T>): Mapper<T> {
        return object: Mapper<T> {
            override fun get(entity: Int): T {
                return edit(entity).getComponent(component)
            }

            override fun contains(entity: Int) = edit(entity).hasComponent(component)

            override fun remove(entity: Int) {
                edit(entity).removeComponent(component)
            }

        }
    }

    private fun getListeners(aspect: AspectImpl): MutableList<EntitySubscription.Listener> {
        return listeners.computeIfAbsent(aspect, { LinkedList<EntitySubscription.Listener>() })
    }

    private fun getEntityEditor(entity: Int): EntityEditor {
        var editor = editors[entity]

        if (editor == null) {
            editor = object: EntityEditor {
                override fun <T: Component> getComponent(component: KClass<T>): T {
                    assertEntityAlive(entity)
                    val entityBits = entities[entity]!!
                        .or(componentIdentityManager.getIdentity(component))
                    update(entity, entityBits)

                    return getComponentsOf(entity).computeIfAbsent(component, {
                        createComponent(component)
                    }) as T
                }

                override fun removeComponent(component: KClass<out Component>) {
                    assertEntityAlive(entity)
                    val entityBits = entities[entity]!!
                        .xor(componentIdentityManager.getIdentity(component))

                    update(entity, entityBits)
                    val instance = getComponentsOf(entity).remove(component)
                    if (instance != null) {
                        cleanupComponent(instance)
                    }

                }

                override fun hasComponent(component: KClass<out Component>): Boolean {
                    assertEntityAlive(entity)
                    val bits = componentIdentityManager.getIdentity(component)
                    return entities[entity]!!
                        .and(bits) == bits
                }

            }
            editors[entity] = editor
        }

        return editor
    }

    private fun update(entity: Int, entityBits: Bits?) {


        if (entityBits != null) {
            entities[entity] = entityBits
            aspects.forEach {
                val match = it.key.match(entityBits)
                val previouslySet = it.value[entity]
                aspects[it.key] = it.value.set(entity, match)

                val listeners = getListeners(it.key)
                if (previouslySet && !match) {
                    listeners.forEach {
                        it.entityRemoved(entity)
                    }
                } else if (!previouslySet && match) {
                    listeners.forEach {
                        it.entityAdded(entity)
                    }
                }
            }
        } else {
            val oldValue = entities[entity]!!
            entities.remove(entity)
            aspects.forEach {
                if (it.key.match(oldValue)) {
                    it.value.set(entity, false)
                    listeners[it.key]!!.forEach {
                        it.entityRemoved(entity)
                    }
                }
            }
        }

    }

    private fun getComponentsOf(entity: Int): HashMap<KClass<out Component>, Component> {
        return if (components.isPresent(entity)) {
            components[entity]!!
        } else {
            HashMap<KClass<out Component>, Component>().apply {
                components[entity] = this
            }
        }
    }

    private fun <T: Component> createComponent(component: KClass<T>): T {
        return componentFactories.factoryFor(component)
            .create()
    }

    private fun <T: Component> cleanupComponent(component: T) {
        componentFactories.factoryFor(component.javaClass.kotlin)
            .release(component)
    }

    private fun assertEntityAlive(entity: Int) {
        if (!isEntityAlive(entity)) {
            throw EntityNotFoundException(entity)
        }
    }

    private fun isEntityAlive(entity: Int): Boolean {
        return entities[entity] != null
    }

}
