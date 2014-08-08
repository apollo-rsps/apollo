package org.apollo.game.message.handler.impl;

import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerContext;
import org.apollo.game.message.impl.NpcActionMessage;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Npc;
import org.apollo.game.model.entity.Player;
import org.apollo.util.MobRepository;

/**
 * A verification {@link MessageHandler} for the {@link NpcActionMessage}.
 *
 * @author Stuart
 * @author Major
 */
public final class NpcActionVerificationHandler extends MessageHandler<NpcActionMessage> {

	@Override
	public void handle(MessageHandlerContext ctx, Player player, NpcActionMessage message) {
		MobRepository<Npc> repository = World.getWorld().getNpcRepository();
		int index = message.getIndex();

		if (index < 0 || index >= repository.capacity()) {
			ctx.breakHandlerChain();
			return;
		}

		Npc npc = repository.get(index);

		if (npc == null || !player.getPosition().isWithinDistance(npc.getPosition(), 15)) {
			ctx.breakHandlerChain();
			return;
		}

		if (message.getOption() >= npc.getDefinition().getInteractions().length) {
			ctx.breakHandlerChain();
			return;
		}
	}

}