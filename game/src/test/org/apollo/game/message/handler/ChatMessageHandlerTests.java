package org.apollo.game.message.handler;

import org.apollo.game.message.impl.ChatMessage;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;


@RunWith(PowerMockRunner.class)
@PrepareForTest({Player.class})
public final class ChatMessageHandlerTests {

	private final World world = new World();
	private final ChatMessageHandler chatMessageHandler = new ChatMessageHandler(world);

	@Test
	public void terminateIfMuted() throws Exception {
		Player player = PowerMockito.mock(Player.class);

		when(player.isMuted()).thenReturn(true);

		ChatMessage chatMessage = new ChatMessage("Test", "Test".getBytes(), 0, 0);
		chatMessageHandler.handle(player, chatMessage);

		assertTrue("ChatMessageHandler: player can send messages when muted", chatMessage.terminated());
	}
}