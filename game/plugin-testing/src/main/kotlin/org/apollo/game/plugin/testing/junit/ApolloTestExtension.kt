package org.apollo.game.plugin.testing.junit

import io.mockk.every
import io.mockk.slot
import io.mockk.spyk
import io.mockk.staticMockk
import org.apollo.cache.def.ItemDefinition
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
import org.apollo.game.plugin.testing.junit.mocking.StubPrototype
import org.junit.jupiter.api.extension.*
import java.util.*
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createType
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
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

        val pluginEnvironment = KotlinPluginEnvironment(stubWorld)
        pluginEnvironment.setContext(FakePluginContextFactory.create(stubHandlers))
        pluginEnvironment.load(ArrayList<PluginMetaData>())

        val state = ApolloTestState(stubHandlers, stubWorld)
        val store = context.getStore(namespace)
        store.put(ApolloTestState::class, state)
    }

    override fun beforeEach(context: ExtensionContext) {
        val testClass = context.requiredTestClass.kotlin
        val testClassInstance = context.requiredTestInstance
        val testClassProps = testClass.declaredMemberProperties
        val testClassMethods = context.testClass.map { it.kotlin.declaredMemberFunctions }.orElse(emptyList())
        val testClassItemDefs = testClassMethods.asSequence()
            .mapNotNull { it.findAnnotation<ItemDefinitions>()?.let { anno -> it to anno } }
            .flatMap { (it.first.call(context.requiredTestInstance as Any) as Collection<ItemDefinition>).asSequence() }
            .map { it.id to it }
            .toMap()

        if (testClassItemDefs.isNotEmpty()) {
            val itemIdSlot = slot<Int>()

            staticMockk<ItemDefinition>().mock()
            every { ItemDefinition.lookup(capture(itemIdSlot)) } answers { testClassItemDefs[itemIdSlot.captured] }
        }

        val store = context.getStore(namespace)
        val state = store.get(ApolloTestState::class) as ApolloTestState

        val propertyStubSites = testClassProps.asSequence()
            .mapNotNull { it as? KMutableProperty<*> }
            .filter { supportedTestDoubleTypes.contains(it.returnType) }

        propertyStubSites.forEach {
            it.setter.call(
                testClassInstance,
                state.createStub(StubPrototype(it.returnType.jvmErasure, it.annotations))
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
}