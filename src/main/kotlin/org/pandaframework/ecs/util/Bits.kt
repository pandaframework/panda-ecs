package org.pandaframework.ecs.util

import java.util.*

/**
 * A memory efficient wrapper for [java.util.BitSet].
 *
 * @author Ranie Jade Ramiso
 */
class Bits private constructor(private val internal: BitSet) {

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

        private fun xor(first: Bits, second: Bits): Bits {
            return compose {
                it.or(first.internal)
                it.xor(second.internal)
            }
        }

        private fun set(bits: Bits, bit: Int, value: Boolean): Bits {
            return compose {
                it.or(bits.internal)
                it.set(bit, value)
            }
        }

        private fun get(bits: Bits, bit: Int): Boolean = bits.internal.get(bit)

        private fun compose(composer: (BitSet) -> Unit): Bits {
            builder.clear()
            composer(builder)
            val clone = builder.clone() as BitSet
            return cache.getOrPut(clone, { Bits(clone) })
        }
    }

    fun and(other: Bits): Bits = and(this, other)

    fun or(other: Bits): Bits = or(this, other)

    fun xor(other: Bits): Bits = xor(this, other)

    fun set(bit: Int): Bits = set(this, bit, true)

    fun set(bit: Int, value: Boolean): Bits = set(this, bit, value)

    fun isEmpty() = internal.isEmpty

    fun setBits(): List<Int> {
        val list = LinkedList<Int>()
        internal.stream()
            .forEach {
                list.add(it)
            }

        return list
    }

    operator fun get(bit: Int): Boolean = get(this, bit)

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
