package org.pandaframework.ecs.component

/**
 * Fast way to access a particular component.
 *
 * @author Ranie Jade Ramiso
 */
interface ComponentMapper<out T: Component> {

    /**
     * Retrieve an existing or create a new instance of [T] for [entity]
     *
     * ## Sample usage
     * ```
     * mapper.get(entity)
     * // or
     * mapper[entity]
     * ```
     * @param entity entity who's [T] component to fetch.
     * @return existing instance or a new one if none is present.
     */
    operator fun get(entity: Int): T

    /**
     * Remove or create component [T] for [entity].
     *
     * ## Sample usage
     * ```
     * val enabled = true // false
     * mapper.set(entity, enabled)
     * // or
     * mapper[entity] = enabled
     * ```
     *
     * @param entity entity who's [T] component to set.
     */
    operator fun set(entity: Int, value: Boolean)

    /**
     * Check whether [entity] has the component [T].
     *
     * @param entity entity to check for existence of [T].
     * @return true if [entity] has the component [T].
     */
    fun has(entity: Int): Boolean

    /**
     * Remove the component [T] from [entity].
     *
     * This does nothing if [entity] does not contain [T] in the first place.
     *
     * @param entity entity who's component to be removed.
     */
    fun remove(entity: Int)
}
