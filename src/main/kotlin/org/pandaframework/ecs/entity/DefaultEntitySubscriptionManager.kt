package org.pandaframework.ecs.entity

import org.pandaframework.ecs.component.Component
import org.pandaframework.ecs.component.ComponentIdentityManager
import org.pandaframework.ecs.system.AbstractSystem
import org.pandaframework.ecs.util.Bag
import org.pandaframework.ecs.util.Bits
import org.pandaframework.ecs.util.identity.IdentityFactories
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.primaryConstructor

/**
 * @author Ranie Jade Ramiso
 */
class DefaultEntitySubscriptionManager(private val componentIdentityManager: ComponentIdentityManager)
    : EntitySubscriptionManager {
    val identityFactory = IdentityFactories.recycling()

    val entities = Bag<Bits>()
    val components = Bag<MutableSet<Component>>()
    val editors = Bag<EntityEditor>()
    val aspectMap = HashMap<KClass<out AbstractSystem>, AspectImpl>()
    val aspects: MutableMap<AspectImpl, Bits> = HashMap()

    override fun subscribe(aspect: Aspect): EntitySubscription {
        TODO()
    }

    override fun create(): Int {
        val entity = identityFactory.generate()
        entities[entity] = Bits()
        return entity
    }

    override fun remove(entity: Int) {
        TODO()
    }

    override fun edit(entity: Int): EntityEditor {
        assertEntityAlive(entity)
        return getEntityEditor(entity)
    }

    override fun aspectFor(system: KClass<out AbstractSystem>): Aspect {
        return aspectMap.computeIfAbsent(system, {
            val aspect = AspectImpl(componentIdentityManager)
            aspects.put(aspect, Bits())
            aspect
        })
    }

    private fun getEntityEditor(entity: Int): EntityEditor {
        var editor = editors[entity]

        if (editor == null) {
            editor = object: EntityEditor {
                override fun <T: Component> addComponent(component: KClass<T>): T {
                    val entityBits = entities[entity]!!
                        .or(componentIdentityManager.getIdentity(component))
                    update(entity, entityBits)

                    return createComponent(component).apply {
                        getComponentsOf(entity).add(this)
                    }
                }

                override fun removeComponent(component: KClass<out Component>) {
                    TODO()
                }

                override fun hasComponent(component: KClass<out Component>): Boolean {
                    val bits = componentIdentityManager.getIdentity(component)
                    return entities[entity]!!
                        .and(bits) == bits
                }

            }
            editors[entity] = editor
        }

        return editor
    }

    private fun update(entity: Int, entityBits: Bits) {
        entities[entity] = entityBits
        aspects.forEach {
            if (it.key.match(entityBits)) {
                it.value.set(entity)
            } else {
                it.value.set(entity, false)
            }
        }

    }

    private fun getComponentsOf(entity: Int): MutableSet<Component> {
        return if (components.isPresent(entity)) {
            components[entity]!!
        } else {
            HashSet<Component>().apply {
                components[entity] = this
            }
        }
    }

    private fun <T: Component> createComponent(component: KClass<T>): T {
        return component.primaryConstructor!!.call()
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
