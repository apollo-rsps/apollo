package org.apollo.game.plugin.testing.junit

import io.mockk.every
import io.mockk.slot
import io.mockk.spyk
import io.mockk.staticMockk
import org.apollo.cache.def.ItemDefinition
import org.apollo.game.action.Action
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
import org.apollo.game.plugin.testing.junit.stubs.PlayerStubInfo
import org.junit.jupiter.api.extension.*
import java.lang.reflect.Method
import java.util.*

internal val supportedTestParamTypes = setOf(
    Player::class,
    Npc::class,
    GameObject::class,
    World::class,
    ActionCapture::class
)

class ApolloTestingExtension : BeforeEachCallback, AfterEachCallback, ParameterResolver {
    private val namespace = ExtensionContext.Namespace.create("apollo")

    private fun testMethodNeedsSetup(method: Method): Boolean {
        return method.parameterTypes.any { supportedTestParamTypes.contains(it.kotlin) }
    }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        val param = parameterContext.parameter
        val paramType = param.type.kotlin

        return supportedTestParamTypes.contains(paramType)
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
        val param = parameterContext.parameter
        val paramType = param.type.kotlin
        val testStore = extensionContext.getStore(namespace)
        val testState = testStore.get(ApolloTestState::class) as ApolloTestState

        return when (paramType) {
            Player::class -> testState.createPlayer(PlayerStubInfo.create(param.annotations))
            World::class -> testState.world
            ActionCapture::class -> testState.createActionCapture(Action::class)
            else -> throw IllegalArgumentException("Unsupported parameter type {${paramType.qualifiedName}")
        }
    }

    override fun beforeEach(context: ExtensionContext) {
        val testClassMethods = context.requiredTestClass.declaredMethods
        val testItemDefs = testClassMethods.asSequence()
            .filter { it.isAnnotationPresent(ItemDefinitions::class.java) }
            .flatMap { (it.invoke(context.requiredTestInstance as Any) as Collection<ItemDefinition>).asSequence() }
            .map { it.id to it }
            .toMap()

        if (testItemDefs.isNotEmpty()) {
            val itemIdSlot = slot<Int>()

            staticMockk<ItemDefinition>().mock()
            every { ItemDefinition.lookup(capture(itemIdSlot)) } answers { testItemDefs[itemIdSlot.captured] }
        }

        if (testMethodNeedsSetup(context.requiredTestMethod)) {
            val testStore = context.getStore(namespace)

            val stubHandlers = MessageHandlerChainSet()
            val stubWorld = spyk(World())

            val pluginEnvironment = KotlinPluginEnvironment(stubWorld)
            pluginEnvironment.setContext(FakePluginContextFactory.create(stubHandlers))
            pluginEnvironment.load(ArrayList<PluginMetaData>())

            testStore.put(ApolloTestState::class, ApolloTestState(stubHandlers, stubWorld))
        }
    }

    override fun afterEach(context: ExtensionContext) {
        if (testMethodNeedsSetup(context.requiredTestMethod)) {
            val testStore = context.getStore(namespace)
            val testState = testStore.get(ApolloTestState::class) as ApolloTestState

            testState.actionCapture?.runAction()

            testState.reset()
            testStore.remove(ApolloTestState::class)
        }
    }
}