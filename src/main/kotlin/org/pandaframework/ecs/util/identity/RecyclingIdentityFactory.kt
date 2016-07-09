package org.pandaframework.ecs.util.identity

import java.util.*

class RecyclingIdentityFactory (delegate: IdentityFactory): ForwardingIdentityFactory(delegate) {
    private val recycled: BitSet = BitSet()
    private val limbo: MutableList<Int> = mutableListOf()

    override fun generate(): Int {
        if (limbo.isEmpty()) {
            return super.generate()
        }
        val id = limbo.removeAt(0)
        recycled.set(id, false)
        return id
    }

    fun free(identity: Int) {
        limbo.add(identity)
        recycled.set(identity)
    }
}
