package io.polymorphicpanda.panda.ecs.system;

import io.polymorphicpanda.panda.ecs.entity.Aspect;
import io.polymorphicpanda.panda.ecs.entity.EntitySubscription;
import io.polymorphicpanda.panda.ecs.entity.EntitySubscriptionManager;
import io.polymorphicpanda.panda.ecs.util.collection.ImmutableIntBag;

/**
 * @author Ranie Jade Ramiso
 */
public abstract class AbstractSystem implements EntitySubscription.Listener {
    private final EntitySubscription subscription;
    private boolean enabled;

    protected AbstractSystem(Aspect.Builder aspect) {
        subscription = peer.getEntitySubscriptionManager().subscription(aspect);
        subscription.subscribe(this);
        enabled = true;
    }

    public void initialize() {
        // do nothing
    }

    public final void process(float delta) {
        if (isEnabled() && canProcess(delta)) {
            subscription.entities()
                .forEach(entity -> process(delta, entity));
        }
    }

    public void destroy() {
        // do nothing
    }

    protected boolean canProcess(float delta) {
        return true;
    }

    protected boolean isEnabled() {
        return enabled;
    }

    protected void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public final void inserted(ImmutableIntBag entities) {
        entities.forEach(this::inserted);
    }

    @Override
    public final void removed(ImmutableIntBag entities) {
        entities.forEach(this::removed);
    }

    protected abstract void inserted(int entity);
    protected abstract void removed(int entity);
    protected abstract void process(float delta, int entity);


    private static Peer peer;

    /**
     * @treatAsPrivate
     */
    @Deprecated
    public static void impl_setPeer(Peer peer) {
        AbstractSystem.peer = peer;
    }

    /**
     * Bridge between {@link AbstractSystem}s and the {@link io.polymorphicpanda.panda.ecs.World}
     *
     * @treatAsPrivate
     */
    public interface Peer {
        EntitySubscriptionManager getEntitySubscriptionManager();
    }
}
