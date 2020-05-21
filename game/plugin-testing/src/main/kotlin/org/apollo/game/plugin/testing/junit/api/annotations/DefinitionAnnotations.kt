package org.apollo.game.plugin.testing.junit.api.annotations

/**
 * Specifies that the the ItemDefinitions returned by the annotated function should be inserted into the definition
 * table.
 *
 * The annotated function **must**:
 * - Be inside a **companion object** inside an apollo test class (a regular object will not work).
 * - Return a `Collection<ItemDefinition>`.
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ItemDefinitions

/**
 * Specifies that the the NpcDefinitions returned by the annotated function should be inserted into the definition
 * table.
 *
 * The annotated function **must**:
 * - Be inside a **companion object** inside an apollo test class (a regular object will not work).
 * - Return a `Collection<NpcDefinition>`.
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class NpcDefinitions

/**
 * Specifies that the the ObjectDefinitions returned by the annotated function should be inserted into the definition
 * table.
 *
 * The annotated function **must**:
 * - Be inside a **companion object** inside an apollo test class (a regular object will not work).
 * - Return a `Collection<ObjectDefinition>`.
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ObjectDefinitions