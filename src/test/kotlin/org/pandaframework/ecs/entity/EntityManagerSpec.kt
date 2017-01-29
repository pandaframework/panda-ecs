package org.pandaframework.ecs.entity

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.winterbe.expekt.expect
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.subject.SubjectSpek
import org.pandaframework.ecs.aspect.AspectManager
import org.pandaframework.ecs.component.Component
import org.pandaframework.ecs.entity.pool.EntityEditor
import org.pandaframework.ecs.entity.pool.EntityPool

/**
 * @author Ranie Jade Ramiso
 */
object EntityManagerSpec: SubjectSpek<EntityManager>({
    val entityPool = memoized { mock<EntityPool> () }
    val aspectManager = memoized { mock<AspectManager>() }

    subject {
        EntityManager(aspectManager(), entityPool())
    }

    it("create delegates to EntityPool") {
        subject.create()
        verify(entityPool()).create()
    }

    it("destroy delegates to EntityPool") {
        val entity = subject.create()
        subject.destroy(entity)
        verify(entityPool()).destroy(eq(entity))
    }

    on("using a mapper") {
        class Component1: Component
        val editor = mock<EntityEditor>()
        whenever(entityPool().edit(any())).doReturn(editor)

        val mapper = subject.mapper(Component1::class)
        val entity = subject.create()

        it("should use one instance per component") {
            expect(subject.mapper(Component1::class)).satisfy {
                it === mapper
            }
        }

        it("mapper.get delegates to EntityPool") {
            mapper.get(entity)
            verify(editor).get(Component1::class)
        }

        it("mapper.contains delegates to EntityPool") {
            mapper.contains(entity)
            verify(editor).contains(Component1::class)
        }

        it("mapper.get delegates to EntityPool") {
            mapper.remove(entity)
            verify(editor).remove(Component1::class)
        }
    }
})
