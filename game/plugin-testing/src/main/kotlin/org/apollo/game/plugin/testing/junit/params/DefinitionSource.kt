package org.apollo.game.plugin.testing.junit.params

import org.apollo.cache.def.ItemDefinition
import org.apollo.cache.def.NpcDefinition
import org.apollo.cache.def.ObjectDefinition
import org.junit.jupiter.params.provider.ArgumentsSource

/**
 * `@ItemDefinitionSource` is an [ArgumentsSource] for [ItemDefinition]s.
 *
 * @param sourceNames The names of the properties or functions annotated with `@ItemDefinitions` to use as sources of
 * [ItemDefinition]s for the test with this annotation. Every property/function must return
 * `Collection<ItemDefinition>`. If no [sourceNames] are provided, every property and function annotated with
 * `@ItemDefinitions` (in this class's companion object) will be used.
 *
 * @see org.junit.jupiter.params.provider.ArgumentsSource
 * @see org.junit.jupiter.params.ParameterizedTest
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@ArgumentsSource(ItemDefinitionsProvider::class)
annotation class ItemDefinitionSource(vararg val sourceNames: String)

/**
 * `@NpcDefinitionSource` is an [ArgumentsSource] for [NpcDefinition]s.
 *
 * @param sourceNames The names of the properties or functions annotated with `@NpcDefinitions` to use as sources of
 * [NpcDefinition]s for the test with this annotation. Every property/function must return
 * `Collection<NpcDefinition>`. If no [sourceNames] are provided, every property and function annotated with
 * `@NpcDefinitions` (in this class's companion object) will be used.
 *
 * @see org.junit.jupiter.params.provider.ArgumentsSource
 * @see org.junit.jupiter.params.ParameterizedTest
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@ArgumentsSource(NpcDefinitionsProvider::class)
annotation class NpcDefinitionSource(vararg val sourceNames: String)

/**
 * `@ObjectDefinitionSource` is an [ArgumentsSource] for [ObjectDefinition]s.
 *
 * @param sourceNames The names of the properties or functions annotated with `@ObjectDefinitions` to use as sources of
 * [ObjectDefinition]s for the test with this annotation. Every property/function must return
 * `Collection<ObjectDefinition>`. If no [sourceNames] are provided, every property and function annotated with
 * `@ObjectDefinitions` (in this class's companion object) will be used.
 *
 * @see org.junit.jupiter.params.provider.ArgumentsSource
 * @see org.junit.jupiter.params.ParameterizedTest
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@ArgumentsSource(ObjectDefinitionsProvider::class)
annotation class ObjectDefinitionSource(vararg val sourceNames: String)
