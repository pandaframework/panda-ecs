package org.pandaframework.ecs.util.bits;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Ranie Jade Ramiso
 */
public final class Bits {
    private static final Map<BitSet, ImmutableBitSet> CACHE = new HashMap<>();
    private static final BitSet BUILDER = new BitSet();

    public static BitSet compose(Consumer<BitSet> consumer) {
        BUILDER.clear();
        consumer.accept(BUILDER);

        if (CACHE.containsKey(BUILDER)) {
            return CACHE.get(BUILDER);
        }

        final ImmutableBitSet clone = new ImmutableBitSet(BUILDER);

        CACHE.put(clone, clone);

        return clone;
    }

    private Bits() {
    }


    private static final class ImmutableBitSet extends BitSet {
        private ImmutableBitSet(BitSet bitSet) {
            super(bitSet.cardinality());
            super.or(bitSet);
        }

        @Override
        public void flip(int bitIndex) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void flip(int fromIndex, int toIndex) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(int bitIndex) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(int bitIndex, boolean value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(int fromIndex, int toIndex) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(int fromIndex, int toIndex, boolean value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear(int bitIndex) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear(int fromIndex, int toIndex) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void and(BitSet set) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void or(BitSet set) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void xor(BitSet set) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void andNot(BitSet set) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object clone() {
            throw new UnsupportedOperationException();
        }
    }
}
