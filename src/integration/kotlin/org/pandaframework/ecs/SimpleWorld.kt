package org.pandaframework.ecs

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it

/**
 * @author Ranie Jade Ramiso
 */
class SimpleWorld: Spek({
    val world = World.Builder()
        .build()

    it("should pass") { }
})
