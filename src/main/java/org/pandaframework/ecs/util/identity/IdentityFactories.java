package org.pandaframework.ecs.util.identity;

/**
 * @author Ranie Jade Ramiso
 */
public final class IdentityFactories {

    public static IdentityFactory basic() {
        return new BasicIdentityFactory();
    }

    public static RecyclingIdentityFactory recycling() {
        return new RecyclingIdentityFactory(basic());
    }

    private IdentityFactories() {
    }
}
