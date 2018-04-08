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

class ApolloTestingExtension : BeforeEachCallback, AfterEachCallback, ParameterResolver {
    private val namespace = ExtensionContext.Namespace.create("apollo")

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

    override fun beforeEach(context: ExtensionContext) {
        val testClass = context.requiredTestClass.kotlin
        val testClassInstance = context.requiredTestInstance
        val testClassProps = testClass.declaredMemberProperties
        val testClassMethods = testClass.declaredMemberFunctions

        val testItemDefs = testClassMethods.asSequence()
            .mapNotNull { it.findAnnotation<ItemDefinitions>()?.let { anno -> it to anno } }
            .flatMap { (it.first.call(context.requiredTestInstance as Any) as Collection<ItemDefinition>).asSequence() }
            .map { it.id to it }
            .toMap()

        if (testItemDefs.isNotEmpty()) {
            val itemIdSlot = slot<Int>()

            staticMockk<ItemDefinition>().mock()
            every { ItemDefinition.lookup(capture(itemIdSlot)) } answers { testItemDefs[itemIdSlot.captured] }
        }

        val stubHandlers = MessageHandlerChainSet()
        val stubWorld = spyk(World())

        val pluginEnvironment = KotlinPluginEnvironment(stubWorld)
        pluginEnvironment.setContext(FakePluginContextFactory.create(stubHandlers))
        pluginEnvironment.load(ArrayList<PluginMetaData>())

        val testState = ApolloTestState(stubHandlers, stubWorld)

        val propertyStubSites = testClassProps.asSequence()
            .mapNotNull { it as? KMutableProperty<*> }
            .filter { supportedTestDoubleTypes.contains(it.returnType) }

        propertyStubSites.forEach {
            it.setter.call(
                testClassInstance,
                testState.createStub(StubPrototype(it.returnType.jvmErasure, it.annotations))
            )
        }

        val testStore = context.getStore(namespace)
        testStore.put(ApolloTestState::class, testState)
    }

    override fun afterEach(context: ExtensionContext) {
        val testStore = context.getStore(namespace)
        val testState = testStore.get(ApolloTestState::class) as ApolloTestState

        testState.actionCapture?.runAction()

        testState.reset()
        testStore.remove(ApolloTestState::class)
    }
}