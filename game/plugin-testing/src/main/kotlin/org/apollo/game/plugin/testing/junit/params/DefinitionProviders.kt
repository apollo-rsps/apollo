package org.apollo.game.plugin.testing.junit.params

import java.util.stream.Stream
import kotlin.reflect.KCallable
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import org.apollo.cache.def.ItemDefinition
import org.apollo.cache.def.NpcDefinition
import org.apollo.cache.def.ObjectDefinition
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension.Companion.findTestDefinitions
import org.apollo.game.plugin.testing.junit.api.annotations.ItemDefinitions
import org.apollo.game.plugin.testing.junit.api.annotations.NpcDefinitions
import org.apollo.game.plugin.testing.junit.api.annotations.ObjectDefinitions
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.support.AnnotationConsumer

/**
 * An [ArgumentsProvider] for a definition of type `D`.
 */
abstract class DefinitionsProvider<D : Any>(
    private val definitionProvider: (methods: Collection<KCallable<*>>, companionObjectInstance: Any) -> List<D>
) : ArgumentsProvider {

    protected lateinit var sourceNames: Set<String>

    override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
        val companion = context.requiredTestClass.kotlin.companionObject
            ?: throw RuntimeException("${context.requiredTestMethod.name} is annotated with a DefinitionsProvider," +
                " but does not contain a companion object to search for Definitions in."
            )

        val companionInstance = companion.objectInstance!! // safe
        val callables: List<KCallable<*>> = companion.declaredMemberFunctions + companion.declaredMemberProperties

        val filtered = if (sourceNames.isEmpty()) {
            callables
        } else {
            callables.filter { it.name in sourceNames }
        }

        return definitionProvider(filtered, companionInstance).map { Arguments.of(it) }.stream()
    }
}

// These providers are separate because of a JUnit bug in its use of ArgumentsSource and AnnotationConsumer -
// the reflection code that invokes the AnnotationConsumer searches for an accept() method that takes an
// Annotation parameter, prohibiting usage of the actual `Annotation` type as the parameter - meaning
// DefinitionsProvider cannot abstract over different annotation implementations (i.e. over ItemDefinitionSource,
// NpcDefinitionSource, and ObjectDefinitionSource).

/**
 * An [ArgumentsProvider] for [ItemDefinition]s.
 *
 * Test authors should not need to utilise this class, and should instead annotate their function with
 * [@ItemDefinitionSource][ItemDefinitionSource].
 */
object ItemDefinitionsProvider : DefinitionsProvider<ItemDefinition>(
    { methods, companion -> findTestDefinitions<ItemDefinition, ItemDefinitions>(methods, companion) }
), AnnotationConsumer<ItemDefinitionSource> {

    override fun accept(source: ItemDefinitionSource) {
        sourceNames = source.sourceNames.toHashSet()
    }
}

/**
 * An [ArgumentsProvider] for [NpcDefinition]s.
 *
 * Test authors should not need to utilise this class, and should instead annotate their function with
 * [@NpcDefinitionSource][NpcDefinitionSource].
 */
object NpcDefinitionsProvider : DefinitionsProvider<NpcDefinition>(
    { methods, companion -> findTestDefinitions<NpcDefinition, NpcDefinitions>(methods, companion) }
), AnnotationConsumer<NpcDefinitionSource> {

    override fun accept(source: NpcDefinitionSource) {
        sourceNames = source.sourceNames.toHashSet()
    }
}

/**
 * An [ArgumentsProvider] for [ObjectDefinition]s.
 *
 * Test authors should not need to utilise this class, and should instead annotate their function with
 * [@ObjectDefinitionSource][ObjectDefinitionSource].
 */
object ObjectDefinitionsProvider : DefinitionsProvider<ObjectDefinition>(
    { methods, companion -> findTestDefinitions<ObjectDefinition, ObjectDefinitions>(methods, companion) }
), AnnotationConsumer<ObjectDefinitionSource> {

    override fun accept(source: ObjectDefinitionSource) {
        sourceNames = source.sourceNames.toHashSet()
    }
}
