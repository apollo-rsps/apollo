package org.apollo.game.plugin.testing.fakes

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.apollo.game.message.handler.MessageHandler
import org.apollo.game.message.handler.MessageHandlerChainSet
import org.apollo.game.plugin.PluginContext
import org.apollo.net.message.Message

object FakePluginContextFactory {
    fun create(messageHandlers: MessageHandlerChainSet): PluginContext {
        val ctx = mockk<PluginContext>()
        val typeCapture = slot<Class<Message>>()
        val handlerCapture = slot<MessageHandler<Message>>()

        every {
            ctx.addMessageHandler(capture(typeCapture), capture(handlerCapture))
        } answers {
            messageHandlers.putHandler(typeCapture.captured, handlerCapture.captured)
        }

        return ctx
    }
}