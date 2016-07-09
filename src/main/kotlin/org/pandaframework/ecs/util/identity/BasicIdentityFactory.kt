package org.pandaframework.ecs.util.identity

internal class BasicIdentityFactory: IdentityFactory {
    private var identityCount = 0

    override fun generate(): Int {
        return identityCount++
    }
}
