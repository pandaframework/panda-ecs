package org.pandaframework.ecs.util.identity

/**
 * @author Ranie Jade Ramiso
 */
interface IdentityFactory {
    /**
     * Generate a new identity.
     *
     * @return a new identity, this will never be zero.
     */
    fun generate(): Int
}
