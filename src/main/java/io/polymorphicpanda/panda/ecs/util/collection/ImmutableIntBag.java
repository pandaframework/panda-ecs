package io.polymorphicpanda.panda.ecs.util.collection;

/**
 * @author Ranie Jade Ramiso
 */
public interface ImmutableIntBag {
    int get(int index) throws IndexOutOfBoundsException;

    boolean isEmpty();

    int getSize();

    boolean contains(int value);

    void forEach(IntConsumer consumer);

}
