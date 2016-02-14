package org.pandaframework.ecs.util.identity

/**
 * @author Ranie Jade Ramiso
 */
class BasicIdentityFactorySpec extends BaseIdentityFactorySpec<IdentityFactory> {
    @Override
    IdentityFactory getIdentityFactory() {
        return IdentityFactories.basic()
    }
}
