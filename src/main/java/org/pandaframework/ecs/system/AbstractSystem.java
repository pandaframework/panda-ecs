package org.pandaframework.ecs.system;

import org.pandaframework.ecs.World;
import org.pandaframework.ecs.entity.Aspect;
import org.pandaframework.ecs.entity.EntitySubscription;
import org.pandaframework.ecs.entity.EntitySubscriptionManager;
import org.pandaframework.ecs.util.collection.ImmutableIntBag;

/**
 * @author Ranie Jade Ramiso
 */
public abstract class AbstractSystem implements EntitySubscription.Listener {
    private final Aspect.Builder aspect;
    private EntitySubscription subscription;
    private boolean enabled = true;
    private Peer peer;

    protected AbstractSystem(Aspect.Builder aspect) {
        this.aspect = aspect;
    }

    public void initialize() {
        subscription = peer.getEntitySubscriptionManager().subscription(aspect);
        subscription.subscribe(this);
    }

    public /* non-final for testing purposes */ void process(float delta) {
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


    /**
     * @treatAsPrivate
     */
    @Deprecated
    public void impl_setPeer(Peer peer) {
        this.peer = peer;
    }

    /**
     * Bridge between {@link AbstractSystem}s and the {@link World}
     *
     * @treatAsPrivate
     */
    public interface Peer {
        EntitySubscriptionManager getEntitySubscriptionManager();
    }
}
