package org.apollo.game.plugin.testing.fakes

import org.apollo.game.message.handler.MessageHandler
import org.apollo.game.message.handler.MessageHandlerChainSet
import org.apollo.game.plugin.PluginContext
import org.apollo.net.message.Message
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.powermock.api.mockito.PowerMockito

object FakePluginContextFactory {
    fun create(messageHandlers: MessageHandlerChainSet): PluginContext {
        val answer = Answer<Any?> { invocation: InvocationOnMock ->
            messageHandlers.putHandler(
                    invocation.arguments[0] as Class<Message>,
                    invocation.arguments[1] as MessageHandler<*>)
        }

        return PowerMockito.mock(PluginContext::class.java, answer)
    }
}