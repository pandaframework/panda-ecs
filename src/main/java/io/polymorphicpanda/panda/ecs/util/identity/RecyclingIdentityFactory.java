package io.polymorphicpanda.panda.ecs.util.identity;

import java.util.BitSet;

import io.polymorphicpanda.panda.ecs.util.collection.IntDeque;

/**
 * @author Ranie Jade Ramiso
 */
public class RecyclingIdentityFactory extends ForwardingIdentityFactory {
    private final BitSet recycled = new BitSet();
    private final IntDeque limbo = new IntDeque();

    public RecyclingIdentityFactory(IdentityFactory delegate) {
        super(delegate);
    }

    @Override
    public int generate() {
        if (!limbo.isEmpty()) {
            int id = limbo.poll();
            recycled.set(id, false);
            return id;
        }
        return super.generate();
    }

    public void free(int id) {
        limbo.insert(id);
        recycled.set(id);
    }
}
