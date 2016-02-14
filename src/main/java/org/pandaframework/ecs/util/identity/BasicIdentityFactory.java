package org.pandaframework.ecs.util.identity;

/**
 * @author Ranie Jade Ramiso
 */
class BasicIdentityFactory implements IdentityFactory {
    private int identityCount;

    @Override
    public int generate() {
        return identityCount++;
    }
}
