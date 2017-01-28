package org.pandaframework.ecs.entity.pool

import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek
import org.pandaframework.ecs.component.ComponentManager
import kotlin.system.measureNanoTime

/**
 * @author Ranie Jade Ramiso
 */
object BasicEntityPoolBenchmark: SubjectSpek<EntityPool>({
    subject { BasicEntityPool(ComponentManager()) }

    it("n entities") {
        for (i in 0..100000) {
            val result = measureNanoTime {
                subject.create()
            }

            println("result: $result")
        }
    }
})
