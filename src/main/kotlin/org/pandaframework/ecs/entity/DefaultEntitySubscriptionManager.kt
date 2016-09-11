package org.pandaframework.ecs.entity

import org.pandaframework.ecs.component.Component
import org.pandaframework.ecs.component.ComponentFactories
import org.pandaframework.ecs.component.ComponentIdentityManager
import org.pandaframework.ecs.util.Bag
import org.pandaframework.ecs.util.Bits
import org.pandaframework.ecs.util.identity.IdentityFactories
import java.util.*
import kotlin.reflect.KClass

/**
 * @author Ranie Jade Ramiso
 */
class DefaultEntitySubscriptionManager(private val componentIdentityManager: ComponentIdentityManager,
                                       private val componentFactories: ComponentFactories)
    : EntitySubscriptionManager {
    val identityFactory = IdentityFactories.recycling()

    val entities = Bag<Bits>()
    val components = Bag<MutableMap<KClass<out Component>, Component>>()
    val editors = Bag<EntityEditor>()
    val aspects = HashMap<AspectImpl, Bits>()

    override fun subscribe(aspect: AspectImpl): EntitySubscription {
        aspects.computeIfAbsent(aspect, {
            Bits()
        })
        return object: EntitySubscription {
            override fun addListener(listener: EntitySubscription.Listener) {
                TODO()
            }

            override fun removeListener(listener: EntitySubscription.Listener) {
                TODO()
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

    private fun getEntityEditor(entity: Int): EntityEditor {
        var editor = editors[entity]

        if (editor == null) {
            editor = object: EntityEditor {
                override fun <T: Component> addComponent(component: KClass<T>): T {
                    assertEntityAlive(entity)
                    val entityBits = entities[entity]!!
                        .or(componentIdentityManager.getIdentity(component))
                    update(entity, entityBits)

                    return createComponent(component).apply {
                        getComponentsOf(entity).put(component, this)
                    }
                }

                override fun <T: Component> getComponent(component: KClass<T>): T? {
                    assertEntityAlive(entity)
                    return getComponentsOf(entity)[component] as T?
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
                aspects[it.key] = it.value.set(entity, it.key.match(entityBits))
            }
        } else {
            val oldValue = entities[entity]!!
            entities.remove(entity)
            aspects.forEach {
                if (it.key.match(oldValue)) {
                    it.value.set(entity, false)
                }
            }
        }

    }

    private fun getComponentsOf(entity: Int): MutableMap<KClass<out Component>, Component> {
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
