package org.apollo.game.release.r317;

import org.apollo.game.message.impl.ClearRegionMessage;
import org.apollo.game.message.impl.CloseInterfaceMessage;
import org.apollo.game.message.impl.ConfigMessage;
import org.apollo.game.message.impl.DisplayCrossbonesMessage;
import org.apollo.game.message.impl.DisplayTabInterfaceMessage;
import org.apollo.game.message.impl.EnterAmountMessage;
import org.apollo.game.message.impl.FlashTabInterfaceMessage;
import org.apollo.game.message.impl.ForwardPrivateChatMessage;
import org.apollo.game.message.impl.FriendServerStatusMessage;
import org.apollo.game.message.impl.GroupedRegionUpdateMessage;
import org.apollo.game.message.impl.IdAssignmentMessage;
import org.apollo.game.message.impl.IgnoreListMessage;
import org.apollo.game.message.impl.LogoutMessage;
import org.apollo.game.message.impl.MobHintIconMessage;
import org.apollo.game.message.impl.NpcSynchronizationMessage;
import org.apollo.game.message.impl.OpenDialogueInterfaceMessage;
import org.apollo.game.message.impl.OpenDialogueOverlayMessage;
import org.apollo.game.message.impl.OpenInterfaceMessage;
import org.apollo.game.message.impl.OpenInterfaceSidebarMessage;
import org.apollo.game.message.impl.OpenOverlayMessage;
import org.apollo.game.message.impl.OpenSidebarMessage;
import org.apollo.game.message.impl.PlayerSynchronizationMessage;
import org.apollo.game.message.impl.PositionHintIconMessage;
import org.apollo.game.message.impl.PrivacyOptionMessage;
import org.apollo.game.message.impl.RegionChangeMessage;
import org.apollo.game.message.impl.RemoveObjectMessage;
import org.apollo.game.message.impl.RemoveTileItemMessage;
import org.apollo.game.message.impl.SendFriendMessage;
import org.apollo.game.message.impl.SendObjectMessage;
import org.apollo.game.message.impl.SendPublicTileItemMessage;
import org.apollo.game.message.impl.SendTileItemMessage;
import org.apollo.game.message.impl.ServerChatMessage;
import org.apollo.game.message.impl.SetPlayerActionMessage;
import org.apollo.game.message.impl.SetUpdatedRegionMessage;
import org.apollo.game.message.impl.SetWidgetItemModelMessage;
import org.apollo.game.message.impl.SetWidgetModelAnimationMessage;
import org.apollo.game.message.impl.SetWidgetModelMessage;
import org.apollo.game.message.impl.SetWidgetNpcModelMessage;
import org.apollo.game.message.impl.SetWidgetPlayerModelMessage;
import org.apollo.game.message.impl.SetWidgetTextMessage;
import org.apollo.game.message.impl.SetWidgetVisibilityMessage;
import org.apollo.game.message.impl.SwitchTabInterfaceMessage;
import org.apollo.game.message.impl.UpdateItemsMessage;
import org.apollo.game.message.impl.UpdateRunEnergyMessage;
import org.apollo.game.message.impl.UpdateSkillMessage;
import org.apollo.game.message.impl.UpdateSlottedItemsMessage;
import org.apollo.game.message.impl.UpdateTileItemMessage;
import org.apollo.game.message.impl.UpdateWeightMessage;
import org.apollo.net.meta.PacketMetaDataGroup;
import org.apollo.net.release.Release;

/**
 * A {@link Release} implementation for the 317 protocol.
 *
 * @author Graham
 */
public final class Release317 extends Release {

	/**
	 * The incoming packet lengths array.
	 */
	public static final int[] PACKET_LENGTHS = {
			0, 0, 0, 1, -1, 0, 0, 0, 0, 0, // 0
			0, 0, 0, 0, 8, 0, 6, 2, 2, 0, // 10
			0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20
			0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
			2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40
			0, 0, 0, 12, 0, 0, 0, 8, 8, 0, // 50
			0, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60
			6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70
			0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
			0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90
			0, 13, 0, -1, 0, 0, 0, 0, 0, 0, // 100
			0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110
			1, 0, 6, 0, 0, 0, -1, 0, 2, 6, // 120
			0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
			0, 0, 0, 0, 0, 6, 0, 0, 0, 0, // 140
			0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150
			0, 0, 0, 0, -1, -1, 0, 0, 0, 0, // 160
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
			0, 8, 0, 3, 0, 2, 0, 0, 8, 1, // 180
			0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190
			2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
			4, 0, 0, 0, 7, 8, 0, 0, 10, 0, // 210
			0, 0, 0, 0, 0, 0, -1, 0, 6, 0, // 220
			1, 0, 0, 0, 6, 0, 6, 8, 1, 0, // 230
			0, 4, 0, 0, 0, 0, -1, 0, -1, 4, // 240
			0, 0, 6, 6, 0, 0, // 250
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
		WalkMessageDecoder walkMessageDecoder = new WalkMessageDecoder();
		register(248, walkMessageDecoder);
		register(164, walkMessageDecoder);
		register(98, walkMessageDecoder);

		register(0, new KeepAliveMessageDecoder());
		register(101, new PlayerDesignMessageDecoder());
		register(4, new PublicChatMessageDecoder());
		register(103, new CommandMessageDecoder());
		register(214, new SwitchItemMessageDecoder());

		register(132, new FirstObjectActionMessageDecoder());
		register(252, new SecondObjectActionMessageDecoder());
		register(70, new ThirdObjectActionMessageDecoder());

		register(122, new FirstItemOptionMessageDecoder());
		register(41, new SecondItemOptionMessageDecoder());
		register(16, new ThirdItemOptionMessageDecoder());
		register(75, new FourthItemOptionMessageDecoder());
		register(87, new FifthItemOptionMessageDecoder());

		register(145, new FirstItemActionMessageDecoder());
		register(117, new SecondItemActionMessageDecoder());
		register(43, new ThirdItemActionMessageDecoder());
		register(129, new FourthItemActionMessageDecoder());
		register(135, new FifthItemActionMessageDecoder());

		register(185, new ButtonMessageDecoder());
		register(130, new ClosedInterfaceMessageDecoder());
		register(208, new EnteredAmountMessageDecoder());
		register(40, new DialogueContinueMessageDecoder());
		register(120, new FlashingTabClickedMessageDecoder());

		register(53, new ItemOnItemMessageDecoder());
		register(57, new ItemOnNpcMessageDecoder());
		register(237, new MagicOnItemMessageDecoder());
		register(249, new MagicOnPlayerMessageDecoder());
		register(131, new MagicOnNpcMessageDecoder());

		register(3, new FocusUpdateMessageDecoder());
		register(45, new FlaggedMouseEventMessageDecoder());
		register(241, new MouseClickedMessageDecoder());
		register(86, new ArrowKeyMessageDecoder());
		register(95, new PrivacyOptionMessageDecoder());

		SpamPacketMessageDecoder spamMessageDecoder = new SpamPacketMessageDecoder();
		register(77, spamMessageDecoder);
		register(78, spamMessageDecoder);
		register(165, spamMessageDecoder);
		register(189, spamMessageDecoder);
		register(210, spamMessageDecoder);
		register(226, spamMessageDecoder);
		register(121, spamMessageDecoder);

		register(155, new FirstNpcActionMessageDecoder());
		register(72, new SecondNpcActionMessageDecoder());
		register(17, new ThirdNpcActionMessageDecoder());
		register(21, new FourthNpcActionMessageDecoder());
		register(18, new FifthNpcActionMessageDecoder());

		register(236, new TakeTileItemMessageDecoder());
		register(192, new ItemOnObjectMessageDecoder());

		register(128, new FirstPlayerActionMessageDecoder());
		register(153, new SecondPlayerActionMessageDecoder());
		register(73, new ThirdPlayerActionMessageDecoder());
		register(139, new FourthPlayerActionMessageDecoder());
		register(39, new FifthPlayerActionMessageDecoder());

		register(188, new AddFriendMessageDecoder());
		register(133, new AddIgnoreMessageDecoder());
		register(215, new RemoveFriendMessageDecoder());
		register(74, new RemoveIgnoreMessageDecoder());
		register(126, new PrivateChatMessageDecoder());

		register(218, new ReportAbuseMessageDecoder());

		// register encoders
		register(IdAssignmentMessage.class, new IdAssignmentMessageEncoder());
		register(RegionChangeMessage.class, new RegionChangeMessageEncoder());
		register(ServerChatMessage.class, new ServerMessageMessageEncoder());
		register(PlayerSynchronizationMessage.class, new PlayerSynchronizationMessageEncoder());
		register(OpenInterfaceMessage.class, new OpenInterfaceMessageEncoder());
		register(CloseInterfaceMessage.class, new CloseInterfaceMessageEncoder());
		register(SwitchTabInterfaceMessage.class, new SwitchTabInterfaceMessageEncoder());
		register(LogoutMessage.class, new LogoutMessageEncoder());
		register(UpdateItemsMessage.class, new UpdateItemsMessageEncoder());
		register(UpdateSlottedItemsMessage.class, new UpdateSlottedItemsMessageEncoder());
		register(UpdateSkillMessage.class, new UpdateSkillMessageEncoder());
		register(OpenInterfaceSidebarMessage.class, new OpenInterfaceSidebarMessageEncoder());
		register(EnterAmountMessage.class, new EnterAmountMessageEncoder());
		register(SetWidgetTextMessage.class, new SetWidgetTextMessageEncoder());
		register(NpcSynchronizationMessage.class, new NpcSynchronizationMessageEncoder());

		register(SetWidgetVisibilityMessage.class, new SetWidgetVisibilityMessageEncoder());
		register(SetWidgetItemModelMessage.class, new SetWidgetItemModelMessageEncoder());
		register(SetWidgetNpcModelMessage.class, new SetWidgetNpcModelMessageEncoder());
		register(SetWidgetPlayerModelMessage.class, new SetWidgetPlayerModelMessageEncoder());
		register(SetWidgetModelAnimationMessage.class, new SetWidgetModelAnimationMessageEncoder());
		register(SetWidgetModelMessage.class, new SetWidgetModelMessageEncoder());

		register(ConfigMessage.class, new ConfigMessageEncoder());
		register(DisplayTabInterfaceMessage.class, new DisplayTabInterfaceMessageEncoder());
		register(SetUpdatedRegionMessage.class, new SetUpdatedRegionMessageEncoder());
		register(UpdateRunEnergyMessage.class, new UpdateRunEnergyMessageEncoder());
		register(PrivacyOptionMessage.class, new PrivacyOptionMessageEncoder());
		register(OpenDialogueInterfaceMessage.class, new OpenDialogueInterfaceMessageEncoder());
		register(UpdateWeightMessage.class, new UpdateWeightMessageEncoder());
		register(SetPlayerActionMessage.class, new SetPlayerActionMessageEncoder());
		register(DisplayCrossbonesMessage.class, new DisplayCrossbonesMessageEncoder());

		register(SendPublicTileItemMessage.class, new AddGlobalTileItemMessageEncoder());
		register(SendTileItemMessage.class, new AddTileItemMessageEncoder());
		register(UpdateTileItemMessage.class, new UpdateTileItemMessageEncoder());
		register(RemoveTileItemMessage.class, new RemoveTileItemMessageEncoder());
		register(SendObjectMessage.class, new SendObjectMessageEncoder());
		register(RemoveObjectMessage.class, new RemoveObjectMessageEncoder());

		register(GroupedRegionUpdateMessage.class, new GroupedRegionUpdateMessageEncoder(this));
		register(ClearRegionMessage.class, new ClearRegionMessageEncoder());

		register(ForwardPrivateChatMessage.class, new ForwardPrivateChatMessageEncoder());
		register(FriendServerStatusMessage.class, new FriendServerStatusMessageEncoder());
		register(IgnoreListMessage.class, new IgnoreListMessageEncoder());
		register(SendFriendMessage.class, new SendFriendMessageEncoder());
		register(MobHintIconMessage.class, new MobHintIconMessageEncoder());
		register(PositionHintIconMessage.class, new PositionHintIconMessageEncoder());
		register(FlashTabInterfaceMessage.class, new FlashTabInterfaceMessageEncoder());
		register(OpenSidebarMessage.class, new OpenSidebarMessageEncoder());
		register(OpenOverlayMessage.class, new OpenOverlayMessageEncoder());
		register(OpenDialogueOverlayMessage.class, new OpenDialogueOverlayMessageEncoder());
	}

}