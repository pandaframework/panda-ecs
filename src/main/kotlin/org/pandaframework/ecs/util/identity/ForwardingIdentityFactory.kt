package org.pandaframework.ecs.util.identity

open class ForwardingIdentityFactory (delegate: IdentityFactory): IdentityFactory by delegate
