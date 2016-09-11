package org.pandaframework.ecs.util.pool

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.sameInstance
import org.jetbrains.spek.api.SubjectSpek
import org.jetbrains.spek.api.dsl.it

/**
 * @author Ranie Jade Ramiso
 */
class ObjectPoolSpec: SubjectSpek<ObjectPool<Any>>({
    subject {
        BasicObjectPool(10, { Any() })
    }

    it("should return a unique instance") {
        assertThat(subject.acquire(), !sameInstance(subject.acquire()))
    }

    it("should reuse released instance") {
        val instance = subject.acquire()
        subject.release(instance)

        assertThat(subject.acquire(), sameInstance(instance))
    }
})
