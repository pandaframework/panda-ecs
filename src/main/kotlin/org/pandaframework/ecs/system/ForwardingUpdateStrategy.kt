package org.pandaframework.ecs.system

abstract class ForwardingUpdateStrategy(delegate: UpdateStrategy): UpdateStrategy by delegate
