package org.apollo.game.message.handler;

import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.game.util.mocks.FakeMessage;
import org.apollo.game.util.mocks.FakeMessageHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * @author Lesley
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Player.class)
public final class MessageHandlerChainTest {

	@Test
	public void notify_byDefault_notifiesAddedHandlers() {
		FakeMessageHandler mockMessageHandler = makeFakeMessageHandler();
		Player stubPlayer = makePlayer();
		FakeMessage stubMessage = makeFakeMessage();
		MessageHandlerChain<FakeMessage> chain = makeChain();

		chain.addHandler(mockMessageHandler);
		chain.notify(stubPlayer, stubMessage);

		Player expectedPlayer = stubPlayer;
		Player actualPlayer = mockMessageHandler.lastHandlePlayer;
		assertEquals(expectedPlayer, actualPlayer);

		FakeMessage expectedMessage = stubMessage;
		FakeMessage actualMessage = mockMessageHandler.lastHandleMessage;
		assertEquals(expectedMessage, actualMessage);
	}

	@Test
	public void notify_whenCalledWithTerminatedMessage_doesNotNotifySecondHandlers() {
		FakeMessageHandler stubMessageHandler = makeFakeMessageHandler();
		FakeMessageHandler mockMessageHandler = makeFakeMessageHandler();
		Player stubPlayer = makePlayer();
		FakeMessage stubMessage = makeFakeMessage();
		MessageHandlerChain<FakeMessage> chain = makeChain();

		stubMessage.terminate();
		chain.addHandler(stubMessageHandler);
		chain.addHandler(mockMessageHandler);
		chain.notify(stubPlayer, stubMessage);

		Player expectedPlayer = null;
		Player actualPlayer = mockMessageHandler.lastHandlePlayer;
		assertEquals(expectedPlayer, actualPlayer);

		FakeMessage expectedMessage = null;
		FakeMessage actualMessage = mockMessageHandler.lastHandleMessage;
		assertEquals(expectedMessage, actualMessage);
	}

	private Player makePlayer() {
		return mock(Player.class);
	}

	private FakeMessage makeFakeMessage() {
		return new FakeMessage();
	}

	private World makeWorld() {
		return new World();
	}

	private FakeMessageHandler makeFakeMessageHandler() {
		World world = makeWorld();
		return new FakeMessageHandler(world);
	}

	private MessageHandlerChain<FakeMessage> makeChain() {
		return new MessageHandlerChain(FakeMessage.class);
	}

}