package org.pandaframework.ecs.util.identity

object IdentityFactories {
    fun basic(): IdentityFactory {
        return BasicIdentityFactory()
    }

    fun recycling(): RecyclingIdentityFactory {
        return RecyclingIdentityFactory(basic())
    }

}


