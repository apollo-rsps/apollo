package org.apollo.net.release.r317;

import org.apollo.game.event.impl.CloseInterfaceEvent;
import org.apollo.game.event.impl.EnterAmountEvent;
import org.apollo.game.event.impl.IdAssignmentEvent;
import org.apollo.game.event.impl.LogoutEvent;
import org.apollo.game.event.impl.OpenInterfaceEvent;
import org.apollo.game.event.impl.OpenInterfaceSidebarEvent;
import org.apollo.game.event.impl.PlayerSynchronizationEvent;
import org.apollo.game.event.impl.RegionChangeEvent;
import org.apollo.game.event.impl.ServerMessageEvent;
import org.apollo.game.event.impl.SwitchTabInterfaceEvent;
import org.apollo.game.event.impl.UpdateItemsEvent;
import org.apollo.game.event.impl.UpdateSkillEvent;
import org.apollo.game.event.impl.UpdateSlottedItemsEvent;
import org.apollo.net.meta.PacketMetaDataGroup;
import org.apollo.net.release.Release;

/**
 * An implementation of {@link Release} for the 317 protocol.
 * @author Graham
 */
public final class Release317 extends Release {

	/**
	 * The incoming packet lengths array.
	 */
	public static final int[] PACKET_LENGTHS = {
		0, 0, 0, 1, -1, 0, 0, 0, 0, 0,  // 0
		0, 0, 0, 0, 8, 0, 6, 2, 2, 0,   // 10
		0, 2, 0, 6, 0, 12, 0, 0, 0, 0,  // 20
		0, 0, 0, 0, 0, 8, 4, 0, 0, 2,   // 30
		2, 6, 0, 6, 0, -1, 0, 0, 0, 0,  // 40
		0, 0, 0, 12, 0, 0, 0, 0, 8, 0,  // 50
		0, 8, 0, 0, 0, 0, 0, 0, 0, 0,   // 60
		6, 0, 2, 2, 8, 6, 0, -1, 0, 6,  // 70
		0, 0, 0, 0, 0, 1, 4, 6, 0, 0,   // 80
		0, 0, 0, 0, 0, 3, 0, 0, -1, 0,  // 90
		0, 13, 0, -1, 0, 0, 0, 0, 0, 0, // 100
		0, 0, 0, 0, 0, 0, 0, 6, 0, 0,   // 110
		1, 0, 6, 0, 0, 0, -1, 0, 2, 6,  // 120
		0, 4, 6, 8, 0, 6, 0, 0, 0, 2,   // 130
		0, 0, 0, 0, 0, 6, 0, 0, 0, 0,   // 140
		0, 0, 1, 2, 0, 2, 6, 0, 0, 0,   // 150
		0, 0, 0, 0, -1, -1, 0, 0, 0, 0, // 160
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0,   // 170
		0, 8, 0, 3, 0, 2, 0, 0, 8, 1,   // 180
		0, 0, 12, 0, 0, 0, 0, 0, 0, 0,  // 190
		2, 0, 0, 0, 0, 0, 0, 0, 4, 0,   // 200
		4, 0, 0, 0, 7, 8, 0, 0, 10, 0,  // 210
		0, 0, 0, 0, 0, 0, -1, 0, 6, 0,  // 220
		1, 0, 0, 0, 6, 0, 6, 8, 1, 0,   // 230
		0, 4, 0, 0, 0, 0, -1, 0, -1, 4, // 240
		0, 0, 6, 6, 0, 0,               // 250
	};

	/**
	 * Creates and initialises this release.
	 */
	public Release317() {
		super(317, PacketMetaDataGroup.createFromArray(PACKET_LENGTHS));
		init();
	}

	/**
	 * Initialises this release by registering encoders and decoders.
	 */
	private void init() {
		// register decoders
		register(0, new KeepAliveEventDecoder());
		register(101, new CharacterDesignEventDecoder());
		WalkEventDecoder walkEventDecoder = new WalkEventDecoder();
		register(248, walkEventDecoder);
		register(164, walkEventDecoder);
		register(98, walkEventDecoder);
		register(4, new ChatEventDecoder());
		register(185, new ButtonEventDecoder());
		register(103, new CommandEventDecoder());
		register(214, new SwitchItemEventDecoder());
		register(132, new FirstObjectActionEventDecoder());
		register(252, new SecondObjectActionEventDecoder());
		register(70, new ThirdObjectActionEventDecoder());
		register(41, new EquipEventDecoder());
		register(145, new FirstItemActionEventDecoder());
		register(117, new SecondItemActionEventDecoder());
		register(43, new ThirdItemActionEventDecoder());
		register(129, new FourthItemActionEventDecoder());
		register(135, new FifthItemActionEventDecoder());
		register(130, new ClosedInterfaceEventDecoder());
		register(208, new EnteredAmountEventDecoder());

		// register encoders
		register(IdAssignmentEvent.class, new IdAssignmentEventEncoder());
		register(RegionChangeEvent.class, new RegionChangeEventEncoder());
		register(ServerMessageEvent.class, new ServerMessageEventEncoder());
		register(PlayerSynchronizationEvent.class, new PlayerSynchronizationEventEncoder());
		register(OpenInterfaceEvent.class, new OpenInterfaceEventEncoder());
		register(CloseInterfaceEvent.class, new CloseInterfaceEventEncoder());
		register(SwitchTabInterfaceEvent.class, new SwitchTabInterfaceEventEncoder());
		register(LogoutEvent.class, new LogoutEventEncoder());
		register(UpdateItemsEvent.class, new UpdateItemsEventEncoder());
		register(UpdateSlottedItemsEvent.class, new UpdateSlottedItemsEventEncoder());
		register(UpdateSkillEvent.class, new UpdateSkillEventEncoder());
		register(OpenInterfaceSidebarEvent.class, new OpenInterfaceSidebarEventEncoder());
		register(EnterAmountEvent.class, new EnterAmountEventEncoder());
	}

}
