package org.pandaframework.ecs.util.collection;

import com.google.common.primitives.Ints;

/**
 * Bag that contains integers.
 *
 * @author Ranie Jade Ramiso
 */
public class IntBag implements ImmutableIntBag {
    public static final int DEFAULT_CAPACITY = 64;
    private int[] data;
    private int size;

    public IntBag() {
        this(DEFAULT_CAPACITY);
    }

    public IntBag(int capacity) {
        data = new int[capacity];
        size = 0;
    }

    public void insert(int value) {
        insert(size, value);
    }

    void insert(int index, int value) {
        if (size == data.length) {
            grow();
        }

        // don't bother moving values if it's an append
        if (index < size) {
            // shift values to the left from the specified index
            System.arraycopy(data, index, data, index + 1, getSize() - index);
        }
        data[index] = value;
        size++;

    }

    public void insertAll(ImmutableIntBag other) {
        other.forEach(this::insert);
    }

    public void remove(int index) throws IndexOutOfBoundsException {
        if (index < 0 && index >= getSize()) {
            throw new IndexOutOfBoundsException();
        }

        // shift all values to the left starting from the specified index + 1
        final int srcPos = index + 1;
        System.arraycopy(data, srcPos, data, index, getSize() - (srcPos));
        size--;
    }

    public void removeValue(int value) {
        final int index = Ints.indexOf(data, value);

        if (index != -1) {
            remove(index);
        }

    }

    @Override
    public int get(int index) throws IndexOutOfBoundsException {
        if (index < 0 && index >= getSize()) {
            throw new IndexOutOfBoundsException();
        }
        return data[index];
    }

    public int getCapacity() {
        return data.length;
    }

    public void ensureCapacity(int capacity) {
        if (capacity > getCapacity()) {
            grow(capacity);
        }
    }

    @Override
    public boolean isEmpty() {
        return getSize() == 0;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean contains(int value) {
        for (int i = 0; i <size; i++) {
            if (data[i] == value) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void forEach(IntConsumer consumer) {
        for (int i = 0; i < size; i++) {
            consumer.accept(data[i]);
        }
    }

    private void grow() {
        grow((getCapacity() * 3) / 2 + 1);
    }

    private void grow(int newCapacity) {
        data = Ints.ensureCapacity(data, newCapacity, 0);
    }
}
