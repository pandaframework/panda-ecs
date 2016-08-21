package org.pandaframework.ecs.util

/**
 * @author Ranie Jade Ramiso
 */
class Bag<T> constructor() {
    private var buffer = arrayOfNulls<Any>(0)

    val size: Int
        get() = buffer.size

    fun put(index: Int, value: T) {
        ensureCapacity(index + 1)

        buffer[index] = value

    }

    operator fun set(index: Int, value: T) {
        put(index, value)
    }

    operator fun get(index: Int): T? {
        if (index > buffer.lastIndex) {
            throw IndexOutOfBoundsException()
        }
        return (buffer[index] as T?)
    }

    fun remove(index: Int) {
        if (index > buffer.lastIndex) {
            throw IndexOutOfBoundsException()
        }
        buffer[index] = null
    }

    private fun ensureCapacity(size: Int) {
        if (size > buffer.size) {
            buffer = buffer.copyOf(size)
        }
    }
}
