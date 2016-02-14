package org.pandaframework.ecs.util.collection;

import java.util.NoSuchElementException;

/**
 * @author Ranie Jade Ramiso
 */
public class IntDeque implements ImmutableIntBag {
    private final IntBag bag = new IntBag();

    public int poll() throws NoSuchElementException {
        checkIfNotEmpty();

        final int value = get(0);
        bag.remove(0);

        return value;
    }

    public int pollLast() throws NoSuchElementException {
        checkIfNotEmpty();

        final int lastIndex = getSize() - 1;
        final int value = get(lastIndex);
        bag.remove(lastIndex);

        return value;
    }

    public int peek() throws NoSuchElementException {
        checkIfNotEmpty();
        return get(0);
    }

    public int peekLast() throws NoSuchElementException {
        checkIfNotEmpty();
        return get(getSize() - 1);
    }

    public void insert(int value) {
        bag.insert(getSize(), value);
    }

    public void insertFirst(int value) {
        bag.insert(0, value);
    }

    @Override
    public int get(int index) throws IndexOutOfBoundsException {
        return bag.get(index);
    }

    @Override
    public boolean isEmpty() {
        return bag.isEmpty();
    }

    @Override
    public int getSize() {
        return bag.getSize();
    }

    @Override
    public boolean contains(int value) {
        return bag.contains(value);
    }

    @Override
    public void forEach(IntConsumer consumer) {
        bag.forEach(consumer);
    }

    private void checkIfNotEmpty() {
        if (bag.isEmpty()) {
            throw new NoSuchElementException();
        }
    }
}
