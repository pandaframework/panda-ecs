package org.pandaframework.ecs.simple

import com.winterbe.expekt.expect
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.subject.SubjectSpek
import org.pandaframework.ecs.World
import org.pandaframework.ecs.aspect.AspectBuilder
import org.pandaframework.ecs.component.Component
import org.pandaframework.ecs.createWorld
import org.pandaframework.ecs.entity.Entity
import org.pandaframework.ecs.entity.Mapper
import org.pandaframework.ecs.state.State
import org.pandaframework.ecs.state.StateHandler
import org.pandaframework.ecs.system.IteratingSystem
import org.pandaframework.ecs.system.System
import org.pandaframework.ecs.system.UpdateStrategies
import org.pandaframework.ecs.system.UpdateStrategy

sealed class GameState: State {
    object Initial: GameState()
}

data class PositionComponent(var x: Float = 0.0f, var y: Float = 0.0f, var z: Float = 0.0f): Component
data class VelocityComponent(var x: Float = 0.0f, var y: Float = 0.0f, var z: Float = 0.0f): Component

class MovementSystem: System<GameState>(), IteratingSystem {

    private val positionComponentMapper: Mapper<PositionComponent> by mapper()
    private val velocityComponentMapper: Mapper<VelocityComponent> by mapper()

    override val supportedStates: Array<GameState>
        get() = arrayOf(GameState.Initial)

    override fun AspectBuilder.aspect() {
        allOf(PositionComponent::class, VelocityComponent::class)
    }

    override fun updateStrategy(): UpdateStrategy {
        return with(UpdateStrategies) {
            iterating(this@MovementSystem)
        }
    }

    override fun update(time: Double, entity: Entity) {
        val position = positionComponentMapper.get(entity)
        val velocity = velocityComponentMapper.get(entity)

        with(position) {
            x += time.toFloat() * velocity.x
            y += time.toFloat() * velocity.y
            z += time.toFloat() * velocity.z
            println("entity: $entity, position: $position")
        }
    }

}

class InitialStateHandler: StateHandler<GameState.Initial>() {
    private val movingEntityBlueprint by blueprint {
        withComponent<PositionComponent>()
        withComponent<VelocityComponent>()
    }

    private val velocityComponentMapper: Mapper<VelocityComponent> by mapper()

    val entities = mutableListOf<Entity>()
    val positionComponentMapper: Mapper<PositionComponent> by mapper()

    override fun setup() {
        for (i in 1..100) {
            val entity = movingEntityBlueprint.create()
            with(velocityComponentMapper.get(entity)) {
                x = Math.random().toFloat() * 2.0f
                y = Math.random().toFloat() * 2.0f
                z = Math.random().toFloat() * 2.0f
            }
            entities.add(entity)
        }
    }

    override fun cleanup() {
        entities.forEach { entityManager.destroy(it) }
    }

}

class SimpleSpec: SubjectSpek<World<GameState>>({
    val initialStateHandler = memoized { InitialStateHandler() }
    val movementSystem = memoized { MovementSystem() }
    subject {
        createWorld<GameState> {
            initialState(GameState.Initial)
            registerStateHandler(GameState.Initial, initialStateHandler())
            registerSystem(movementSystem())
        }
    }

    beforeGroup {
        subject.setup()
    }

    afterGroup {
        subject.cleanup()
    }

    on("update") {
        try {
            subject.update(5.0)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        it("there should be no entities in the origin") {
            with(initialStateHandler()) {
                entities.forEach {
                    expect(positionComponentMapper.get(it)).satisfy {
                        it!!.x != 0.0f && it.y != 0.0f && it.z != 0.0f
                    }
                }
            }
        }
    }
})
