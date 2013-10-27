package org.apollo.net.release.r377;

import org.apollo.game.event.impl.CloseInterfaceEvent;
import org.apollo.game.event.impl.EnterAmountEvent;
import org.apollo.game.event.impl.IdAssignmentEvent;
import org.apollo.game.event.impl.LogoutEvent;
import org.apollo.game.event.impl.OpenInterfaceEvent;
import org.apollo.game.event.impl.OpenInterfaceSidebarEvent;
import org.apollo.game.event.impl.PlayerSynchronizationEvent;
import org.apollo.game.event.impl.RegionChangeEvent;
import org.apollo.game.event.impl.ServerMessageEvent;
import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.game.event.impl.SwitchTabInterfaceEvent;
import org.apollo.game.event.impl.UpdateItemsEvent;
import org.apollo.game.event.impl.UpdateSkillEvent;
import org.apollo.game.event.impl.UpdateSlottedItemsEvent;
import org.apollo.net.meta.PacketMetaDataGroup;
import org.apollo.net.release.Release;

/**
 * An implementation of {@link Release} for the 377 protocol.
 * 
 * @author Graham
 */
public final class Release377 extends Release {

	/**
	 * The incoming packet lengths array.
	 */
	public static final int[] PACKET_LENGTHS = { 0, 12, 0, 6, 6, 0, 0, 0, 2, 0, // 0
			0, 0, 0, 2, 0, 0, 0, 0, 0, 4, // 10
			0, 0, 2, 0, 6, 0, 0, 0, -1, 0, // 20
			0, 4, 0, 0, 0, 0, 8, 0, 0, 0, // 30
			0, 0, 2, 0, 0, 2, 0, 0, 0, -1, // 40
			6, 0, 0, 0, 6, 6, -1, 8, 0, 0, // 50
			0, 0, 0, 0, 0, 0, 0, 2, 0, 0, // 60
			0, 6, 0, 0, 0, 4, 0, 6, 4, 2, // 70
			2, 0, 0, 8, 0, 0, 0, 0, 0, 0, // 80
			0, 6, 0, 0, 0, 4, 0, 0, 0, 0, // 90
			6, 0, 0, 0, 4, 0, 0, 0, 0, 0, // 100
			0, 0, 2, 0, 0, 0, 2, 0, 0, 1, // 110
			8, 0, 0, 7, 0, 0, 1, 0, 0, 0, // 120
			0, 0, 0, 0, 0, 0, 6, 0, 0, 0, // 130
			4, 8, 0, 8, 0, 0, 0, 0, 0, 0, // 140
			0, 0, 12, 0, 0, 0, 0, 4, 6, 0, // 150
			8, 6, 0, 13, 0, 1, 0, 0, 0, 0, // 160
			0, -1, 0, 3, 0, 0, 3, 6, 0, 0, // 170
			0, 6, 0, 0, 10, 0, 0, 1, 0, 0, // 180
			0, 0, 0, 0, 2, 0, 0, 4, 0, 0, // 190
			0, 0, 0, 6, 0, 0, 8, 0, 0, 0, // 200
			8, 12, 0, -1, 0, 0, 0, 8, 0, 0, // 210
			0, 0, 3, 0, 0, 0, 2, 9, 6, 0, // 220
			6, 6, 0, 2, 0, 0, 0, 0, 0, 0, // 230
			0, 6, 0, 0, -1, 2, 0, -1, 0, 0, // 240
			0, 0, 0, 0, 0, 0 // 250
	};

	/**
	 * Creates and initialises this release.
	 */
	public Release377() {
		super(377, PacketMetaDataGroup.createFromArray(PACKET_LENGTHS));
		init();
	}

	/**
	 * Initialises this release by registering encoders and decoders.
	 */
	private void init() {
		// register decoders
		register(248, new KeepAliveEventDecoder());
		register(163, new CharacterDesignEventDecoder());
		WalkEventDecoder walkEventDecoder = new WalkEventDecoder();
		register(213, walkEventDecoder);
		register(28, walkEventDecoder);
		register(247, walkEventDecoder);
		register(49, new ChatEventDecoder());
		register(79, new ButtonEventDecoder());
		register(56, new CommandEventDecoder());
		register(123, new SwitchItemEventDecoder());
		register(181, new FirstObjectActionEventDecoder());
		register(241, new SecondObjectActionEventDecoder());
		register(50, new ThirdObjectActionEventDecoder());
		register(24, new EquipEventDecoder());
		register(3, new FirstItemActionEventDecoder());
		register(177, new SecondItemActionEventDecoder());
		register(91, new ThirdItemActionEventDecoder());
		register(231, new FourthItemActionEventDecoder());
		register(158, new FifthItemActionEventDecoder());
		register(110, new ClosedInterfaceEventDecoder());
		register(75, new EnteredAmountEventDecoder());

		// register encoders
		register(IdAssignmentEvent.class, new IdAssignmentEventEncoder());
		register(RegionChangeEvent.class, new RegionChangeEventEncoder());
		register(ServerMessageEvent.class, new ServerMessageEventEncoder());
		register(PlayerSynchronizationEvent.class,
				new PlayerSynchronizationEventEncoder());
		register(OpenInterfaceEvent.class, new OpenInterfaceEventEncoder());
		register(CloseInterfaceEvent.class, new CloseInterfaceEventEncoder());
		register(SwitchTabInterfaceEvent.class,
				new SwitchTabInterfaceEventEncoder());
		register(LogoutEvent.class, new LogoutEventEncoder());
		register(UpdateItemsEvent.class, new UpdateItemsEventEncoder());
		register(UpdateSlottedItemsEvent.class,
				new UpdateSlottedItemsEventEncoder());
		register(UpdateSkillEvent.class, new UpdateSkillEventEncoder());
		register(OpenInterfaceSidebarEvent.class,
				new OpenInterfaceSidebarEventEncoder());
		register(EnterAmountEvent.class, new EnterAmountEventEncoder());
		register(SetInterfaceTextEvent.class,
				new SetInterfaceTextEventEncoder());
	}

}
