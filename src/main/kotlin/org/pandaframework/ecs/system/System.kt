package org.pandaframework.ecs.system

import org.pandaframework.ecs.aspect.AspectBuilder
import org.pandaframework.ecs.component.Component
import org.pandaframework.ecs.entity.Blueprint
import org.pandaframework.ecs.entity.BlueprintBuilder
import org.pandaframework.ecs.entity.Entity
import org.pandaframework.ecs.entity.EntityManager
import org.pandaframework.ecs.entity.EntitySubscription
import org.pandaframework.ecs.entity.EntitySubscriptionListener
import org.pandaframework.ecs.entity.Mapper
import org.pandaframework.ecs.state.State
import org.pandaframework.ecs.state.StateManager
import kotlin.properties.Delegates

/**
 * @author Ranie Jade Ramiso
 */
abstract class System<T: State>: EntitySubscriptionListener {
    internal lateinit var _entityManager: EntityManager
    internal lateinit var _stateManager: StateManager<T>
    private var subscription: EntitySubscription by Delegates.notNull()
    private val updateStrategy by lazy { updateStrategy() }

    val entityManager: EntityManager by lazy { _entityManager }
    val stateManager: StateManager<T> by lazy { _stateManager }

    abstract val supportedStates: Array<T>

    abstract fun AspectBuilder.aspect()

    open fun setup() { }

    internal fun update(time: Double) {
        updateStrategy.update(time)
    }

    open fun cleanup() { }

    override fun added(entity: Entity) { }

    override fun removed(entity: Entity) { }

    fun subscription() = subscription

    protected abstract fun updateStrategy(): UpdateStrategy

    protected inline fun <reified T: Component> mapper(): Lazy<Mapper<T>> {
        return lazy {
            entityManager.mapper(T::class)
        }
    }

    protected fun blueprint(block: BlueprintBuilder.() -> Unit): Lazy<Blueprint> {
        return lazy { entityManager.blueprint(block) }
    }

    internal fun bootstrap() {
        subscription = entityManager.subscribe {
            aspect()
        }

        subscription.addListener(this)
    }
}
