package org.pandaframework.ecs.util.bits

import java.util.*

/**
 * @author Ranie Jade Ramiso
 */
internal class Bits private constructor(private val internal: BitSet) {

    companion object {
        private val builder = BitSet()
        private val zero = Bits(BitSet())
        private var cache: MutableMap<BitSet, Bits> = mutableMapOf(zero.internal to zero)

        operator fun invoke (): Bits {
            return zero
        }

        private fun and(first: Bits, second: Bits): Bits {
            return compose {
                it.or(first.internal)
                it.and(second.internal)
            }
        }

        private fun or(first: Bits, second: Bits): Bits {
            return compose {
                it.or(first.internal)
                it.or(second.internal)
            }
        }

        private fun compose(composer: (BitSet) -> Unit): Bits {
            builder.clear()
            composer(builder)
            val clone = builder.clone() as BitSet
            return cache.getOrPut(clone, { Bits(clone) })
        }
    }

    fun and(other: Bits): Bits = and(this, other)

    fun or(other: Bits): Bits = or(this, other)

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        } else if (other?.javaClass != javaClass) {
            return false
        }

        other as Bits

        if (internal != other.internal) {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        return internal.hashCode()
    }
}
