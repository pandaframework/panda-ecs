package org.pandaframework.ecs.util.identity

import java.util.*

/**
 * @author Ranie Jade Ramiso
 */
interface IdentityFactory {
    fun generate(): Int
}

object IdentityFactories {
    fun basic(): IdentityFactory {
        return BasicIdentityFactory()
    }

    fun recycling(): RecyclingIdentityFactory {
        return RecyclingIdentityFactory(basic())
    }
}

open class ForwardingIdentityFactory (delegate: IdentityFactory): IdentityFactory by delegate

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

class BasicIdentityFactory: IdentityFactory {
    private var identityCount = 0

    override fun generate(): Int {
        return identityCount++
    }
}

