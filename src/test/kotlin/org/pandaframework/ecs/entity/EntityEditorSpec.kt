package org.pandaframework.ecs.entity

import com.winterbe.expekt.expect
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.subject.SubjectSpek
import org.pandaframework.ecs.component.Component
import org.pandaframework.ecs.component.ComponentManager
import org.pandaframework.ecs.entity.pool.BasicEntityPool
import org.pandaframework.ecs.entity.pool.EntityPool

/**
 * @author Ranie Jade Ramiso
 */
object EntityEditorSpec: SubjectSpek<EntityPool>({
    val componentManager = memoized { ComponentManager() }
    subject { BasicEntityPool(componentManager()) }

    class Component1: Component
    class Component2: Component
    class Component3: Component

    describe("adding components") {
        it("should return the same instance") {
            val entity = subject.create()
            subject.edit(entity).apply {
                expect(get(Component1::class)).equal(get(Component1::class))
            }
        }

        it("should re-use instances") {
            val entity = subject.create()
            subject.edit(entity).apply {
                val first = get(Component3::class)
                remove(Component3::class)
                val second = get(Component3::class)

                expect(first).to.satisfy {
                    it === second
                }
            }
        }
    }

    on("component removal") {
        val entity = subject.create().apply {
            with(subject.edit(this)) {
                get(Component2::class)
            }
        }

        subject.edit(entity).apply {
            remove(Component2::class)

            it("contains should return false") {
                expect(contains(Component2::class)).to.be.`false`
            }
        }


    }
})
