package org.apollo.game.plugin.testing.junit

import io.mockk.every
import io.mockk.slot
import io.mockk.spyk
import io.mockk.staticMockk
import org.apollo.cache.def.ItemDefinition
import org.apollo.cache.def.NpcDefinition
import org.apollo.cache.def.ObjectDefinition
import org.apollo.game.message.handler.MessageHandlerChainSet
import org.apollo.game.model.World
import org.apollo.game.model.entity.Npc
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.obj.GameObject
import org.apollo.game.plugin.KotlinPluginEnvironment
import org.apollo.game.plugin.PluginMetaData
import org.apollo.game.plugin.testing.fakes.FakePluginContextFactory
import org.apollo.game.plugin.testing.junit.api.ActionCapture
import org.apollo.game.plugin.testing.junit.api.annotations.ItemDefinitions
import org.apollo.game.plugin.testing.junit.api.annotations.NpcDefinitions
import org.apollo.game.plugin.testing.junit.api.annotations.ObjectDefinitions
import org.apollo.game.plugin.testing.junit.mocking.StubPrototype
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.AfterTestExecutionCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import java.util.ArrayList
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.createType
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

internal val supportedTestDoubleTypes = setOf(
    Player::class.createType(),
    Npc::class.createType(),
    GameObject::class.createType(),
    World::class.createType(),
    ActionCapture::class.createType()
)

class ApolloTestingExtension :
    AfterTestExecutionCallback,
    BeforeAllCallback,
    AfterAllCallback,
    BeforeEachCallback,
    AfterEachCallback,
    ParameterResolver {

    private val namespace = ExtensionContext.Namespace.create("apollo")

    private fun cleanup(context: ExtensionContext) {
        val store = context.getStore(namespace)
        val state = store.get(ApolloTestState::class) as ApolloTestState

        try {
            state.actionCapture?.runAction()
        } finally {
            state.reset()
        }
    }

    override fun afterAll(context: ExtensionContext) {
        val store = context.getStore(namespace)
        store.remove(ApolloTestState::class)
    }

    override fun afterEach(context: ExtensionContext) = cleanup(context)

    override fun afterTestExecution(context: ExtensionContext) = cleanup(context)

    override fun beforeAll(context: ExtensionContext) {
        val stubHandlers = MessageHandlerChainSet()
        val stubWorld = spyk(World())

        context.testClass // This _must_ come before plugin environment initialisation
            .map { it.kotlin.companionObject }
            .ifPresent { companion ->
                val companionInstance = companion.objectInstance!!
                val testClassMethods = companion.declaredMemberFunctions

                createTestDefinitions<ItemDefinition, ItemDefinitions>(
                    testClassMethods, companionInstance, ItemDefinition::getId, ItemDefinition::lookup
                )

                createTestDefinitions<NpcDefinition, NpcDefinitions>(
                    testClassMethods, companionInstance, NpcDefinition::getId, NpcDefinition::lookup
                )

                createTestDefinitions<ObjectDefinition, ObjectDefinitions>(
                    testClassMethods, companionInstance, ObjectDefinition::getId, ObjectDefinition::lookup
                )
            }

        val pluginEnvironment = KotlinPluginEnvironment(stubWorld)
        pluginEnvironment.setContext(FakePluginContextFactory.create(stubHandlers))
        pluginEnvironment.load(ArrayList<PluginMetaData>())

        val state = ApolloTestState(stubHandlers, stubWorld)
        val store = context.getStore(namespace)
        store.put(ApolloTestState::class, state)
    }

    override fun beforeEach(context: ExtensionContext) {
        val testClassInstance = context.requiredTestInstance
        val testClassProps = context.requiredTestClass.kotlin.declaredMemberProperties

        val store = context.getStore(namespace)
        val state = store.get(ApolloTestState::class) as ApolloTestState

        val propertyStubSites = testClassProps.asSequence()
            .mapNotNull { it as? KMutableProperty<*> }
            .filter { supportedTestDoubleTypes.contains(it.returnType) }

        propertyStubSites.forEach { property ->
            property.setter.call(
                testClassInstance,
                state.createStub(StubPrototype(property.returnType.jvmErasure, property.annotations))
            )
        }
    }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        val param = parameterContext.parameter
        val paramType = param.type.kotlin

        return supportedTestDoubleTypes.contains(paramType.createType())
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
        val param = parameterContext.parameter
        val paramType = param.type.kotlin
        val testStore = extensionContext.getStore(namespace)
        val testState = testStore.get(ApolloTestState::class) as ApolloTestState

        return testState.createStub(StubPrototype(paramType, param.annotations.toList()))
    }

    /**
     * Mocks the definition class of type [D] for any function with the attached annotation [A].
     *
     * @param testClassMethods All of the methods in the class being tested.
     * @param idMapper The map function that returns an id given a definition [D].
     * @param lookup The lookup function that returns an instance of [D] given a definition id.
     */
    private inline fun <reified D : Any, reified A : Annotation> createTestDefinitions(
        testClassMethods: Collection<KFunction<*>>,
        companionObjectInstance: Any?,
        crossinline idMapper: (D) -> Int,
        crossinline lookup: (Int) -> D
    ) {
        val testDefinitions = testClassMethods.asSequence()
            .filter { method -> method.annotations.any { it is A } }
            .flatMap { method ->
                @Suppress("UNCHECKED_CAST")
                method as? KFunction<Collection<D>> ?: throw RuntimeException("${method.name} is annotated with " +
                    "${A::class.simpleName} but does not return Collection<${D::class.simpleName}."
                )

                method.isAccessible = true // lets us call methods in private companion objects
                method.call(companionObjectInstance).asSequence()
            }
            .associateBy(idMapper)

        if (testDefinitions.isNotEmpty()) {
            val idSlot = slot<Int>()

            staticMockk<D>().mock()
            every { lookup(capture(idSlot)) } answers { testDefinitions[idSlot.captured]!! }
        }
    }

}