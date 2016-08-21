package org.pandaframework.ecs.util

import java.util.*

/**
 * @author Ranie Jade Ramiso
 */
class IntBag {
    private var buffer = IntArray(0)
    private val emptyIndexes = HashSet<Int>()

    val size: Int
        get() = buffer.size

    fun put(index: Int, value: Int) {
        ensureCapacity(index + 1)

        buffer[index] = value
        emptyIndexes.remove(index)

    }

    operator fun set(index: Int, value: Int) {
        put(index, value)
    }

    operator fun get(index: Int): Int {
        if (index > buffer.lastIndex) {
            throw IndexOutOfBoundsException()
        }
        return buffer[index]
    }

    fun remove(index: Int) {
        if (index > buffer.lastIndex) {
            throw IndexOutOfBoundsException()
        }
        emptyIndexes.add(index)
    }

    fun isEmpty(index: Int): Boolean {
        if (index > buffer.lastIndex) {
            throw IndexOutOfBoundsException()
        }
        return emptyIndexes.contains(index)
    }

    private fun ensureCapacity(size: Int) {
        if (size > buffer.size) {
            (buffer.lastIndex..(size - 1)).forEach { emptyIndexes.add(it) }
            buffer = buffer.copyOf(size)
        }
    }
}
