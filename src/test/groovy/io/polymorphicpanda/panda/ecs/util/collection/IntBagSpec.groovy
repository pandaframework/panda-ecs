package io.polymorphicpanda.panda.ecs.util.collection

import spock.lang.Specification

/**
 * @author Ranie Jade Ramiso
 */
class IntBagSpec extends Specification {

    def "default capacity"() {
        expect:
            new IntBag().capacity == IntBag.DEFAULT_CAPACITY
    }

    def "initial capacity"(int capacity) {
        expect:
            new IntBag(capacity).capacity == capacity

        where:
            capacity | _
            1        | _
            12       | _
            100      | _
            12345    | _
    }

    def "grow when full"(int capacity) {
        setup:
            def bag = new IntBag(capacity)

        when:
            (1..capacity + 1).each {
                bag.insert(it)
            }

        then:
            bag.capacity > capacity

        where:
            capacity | _
            1        | _
            10       | _
            11241    | _
    }

    def "inserting a value"() {
        setup:
            int value = 12
            def bag = new IntBag()

        when:
            bag.insert(value)

        then:
            bag.contains(value)
            bag.size == 1
            bag.get(0) == value
    }

    def "inserting values from another bag"() {
        setup:
            def bag = new IntBag()
            def otherBag = new IntBag()

            (1..50).each {
                otherBag.insert(it)
            }

        when:
            bag.insertAll(otherBag)

        then:
            !bag.isEmpty()
            bag.size == otherBag.size
    }

    def "removing a value"() {
        setup:
            int value = 12
            def bag = new IntBag()
            bag.insert(1)
            bag.insert(2)
            bag.insert(value)

        when:
            bag.removeValue(value)

        then:
            bag.size == 2
            !bag.contains(value)
    }

    def "removing value that is not contained in the bag is no-op"() {
        setup:
            def bag = new IntBag()

        when:
            bag.removeValue(12)

        then:
            noExceptionThrown()
    }

    def "removing a value by index"(int[] values, int index) {
        setup:
            def bag = new IntBag()
            values.each {
                bag.insert(it)
            }

        when:
            bag.remove(index)

        then:
            bag.size == values.length - 1

        where:
            values                    | index
            [20, 502, 100]            | 2
            [20, 502, 100, 251]       | 0
            [20, 502, 100, 271, 5125] | 4
    }

    def "removing a value by index - out of bounds"(int[] values, int index) {
        setup:
            def bag = new IntBag()
            values.each {
                bag.insert(it)
            }

        when:
            bag.remove(index)

        then:
            thrown(IndexOutOfBoundsException)


        where:
            values             | index
            [1, 2, 3]          | 4
            [21, 51, 613]      | 3
            [21, 51, 613, 281] | -1
            []                 | -1
            []                 | 0
    }

    def "initially empty"() {
        expect:
            new IntBag().isEmpty()
    }

    def "ensure capacity"(int initialCapacity, int newCapacity) {
        setup:
            def bag = new IntBag(initialCapacity)

        when:
            bag.ensureCapacity(newCapacity)

        then:
            bag.capacity > initialCapacity

        where:
            initialCapacity | newCapacity
            25              | 50
            0               | 1
    }

    def "ensure capacity is no-op if requested capacity is less than or equal to the current capacity"(int initialCapacity, int newCapacity) {
        setup:
            def bag = new IntBag(initialCapacity)

        when:
            bag.ensureCapacity(newCapacity)

        then:
            bag.capacity == initialCapacity

        where:
            initialCapacity | newCapacity
            10              | 10
            0               | 0
            15              | 9
    }

    def "forEach should iterate all entries"() {
        setup:
            int size = 100
            def bag = new IntBag()
            (1..size).each {
                bag.insert(it)
            }

        when:
            bag.forEach({
                size--
            })

        then:
            size == 0

    }
}
