package io.polymorphicpanda.panda.ecs.util.collection

import spock.lang.Specification

/**
 * @author Ranie Jade Ramiso
 */
class IntDequeSpec extends Specification {

    def "peek"() {
        setup:
            def deque = new IntDeque()
            (1..2).each {
                deque.insert(it)
            }

        expect:
            deque.peek() == 1
    }

    def "peek - empty"() {
        when:
            new IntDeque().peek()

        then:
            thrown(NoSuchElementException)
    }

    def "peekLast"() {
        setup:
            def deque = new IntDeque()
            (1..2).each {
                deque.insert(it)
            }

        expect:
            deque.peekLast() == 2
    }

    def "peekLast - empty"() {
        when:
            new IntDeque().peekLast()

        then:
            thrown(NoSuchElementException)
    }

    def "poll"() {
        setup:
            def deque = new IntDeque()
            deque.insert(1)

        expect:
            deque.poll() == 1
            deque.isEmpty()
    }

    def "poll - empty"() {
        when:
            new IntDeque().poll()

        then:
            thrown(NoSuchElementException)
    }

    def "pollLast"() {
        setup:
            def deque = new IntDeque()
            (1..2).each {
                deque.insert(it)
            }

        expect:
            deque.pollLast() == 2
            deque.size == 1
    }

    def "pollLast - empty"() {
        when:
            new IntDeque().pollLast()

        then:
            thrown(NoSuchElementException)
    }

    def "insert"() {
        setup:
            def deque = new IntDeque()
            (2..3).each {
                deque.insert(it)
            }

        when:
            deque.insert(1)

        then:
            deque.peekLast() == 1
    }

    def "insertFirst"() {
        setup:
            def deque = new IntDeque()
            (2..3).each {
                deque.insert(it)
            }

        when:
            deque.insertFirst(1)

        then:
            deque.peek() == 1
    }
}
