package org.apollo.game.release.r377;

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
import org.apollo.game.message.impl.SendProjectileMessage;
import org.apollo.game.message.impl.SendPublicTileItemMessage;
import org.apollo.game.message.impl.SendTileItemMessage;
import org.apollo.game.message.impl.ServerChatMessage;
import org.apollo.game.message.impl.SetPlayerActionMessage;
import org.apollo.game.message.impl.SetUpdatedRegionMessage;
import org.apollo.game.message.impl.SetWidgetItemModelMessage;
import org.apollo.game.message.impl.SetWidgetModelAnimationMessage;
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
 * A {@link Release} implementation for the 377 protocol.
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
		WalkMessageDecoder walkMessageDecoder = new WalkMessageDecoder();
		register(213, walkMessageDecoder);
		register(28, walkMessageDecoder);
		register(247, walkMessageDecoder);

		register(248, new KeepAliveMessageDecoder());
		register(163, new PlayerDesignMessageDecoder());
		register(49, new PublicChatMessageDecoder());
		register(56, new CommandMessageDecoder());
		register(123, new SwitchItemMessageDecoder());

		register(181, new FirstObjectActionMessageDecoder());
		register(241, new SecondObjectActionMessageDecoder());
		register(50, new ThirdObjectActionMessageDecoder());

		register(203, new FirstItemOptionMessageDecoder());
		register(24, new SecondItemOptionMessageDecoder());
		register(161, new ThirdItemOptionMessageDecoder());
		register(228, new FourthItemOptionMessageDecoder());
		register(4, new FifthItemOptionMessageDecoder());

		register(3, new FirstItemActionMessageDecoder());
		register(177, new SecondItemActionMessageDecoder());
		register(91, new ThirdItemActionMessageDecoder());
		register(231, new FourthItemActionMessageDecoder());
		register(158, new FifthItemActionMessageDecoder());

		register(79, new ButtonMessageDecoder());
		register(110, new ClosedInterfaceMessageDecoder());
		register(75, new EnteredAmountMessageDecoder());
		register(226, new DialogueContinueMessageDecoder());
		register(119, new FlashingTabClickedMessageDecoder());

		register(1, new ItemOnItemMessageDecoder());
		register(57, new ItemOnNpcMessageDecoder());
		register(36, new MagicOnItemMessageDecoder());
		register(31, new MagicOnPlayerMessageDecoder());
		register(104, new MagicOnNpcMessageDecoder());

		register(187, new FocusUpdateMessageDecoder());
		register(19, new MouseClickedMessageDecoder());
		register(171, new FlaggedMouseEventMessageDecoder());
		register(140, new ArrowKeyMessageDecoder());
		register(176, new PrivacyOptionMessageDecoder());

		SpamPacketMessageDecoder spamMessageDecoder = new SpamPacketMessageDecoder();
		register(40, spamMessageDecoder);
		register(244, spamMessageDecoder);

		register(112, new FirstNpcActionMessageDecoder());
		register(67, new SecondNpcActionMessageDecoder());
		register(13, new ThirdNpcActionMessageDecoder());
		register(42, new FourthNpcActionMessageDecoder());
		register(8, new FifthNpcActionMessageDecoder());

		register(71, new TakeTileItemMessageDecoder());
		register(152, new ItemOnObjectMessageDecoder());

		register(245, new FirstPlayerActionMessageDecoder());
		register(233, new SecondPlayerActionMessageDecoder());
		register(194, new ThirdPlayerActionMessageDecoder());
		register(116, new FourthPlayerActionMessageDecoder());
		register(45, new FifthPlayerActionMessageDecoder());

		register(120, new AddFriendMessageDecoder());
		register(217, new AddIgnoreMessageDecoder());
		register(141, new RemoveFriendMessageDecoder());
		register(160, new RemoveIgnoreMessageDecoder());
		register(227, new PrivateChatMessageDecoder());

		register(184, new ReportAbuseMessageDecoder());

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
		register(SendProjectileMessage.class, new SendProjectileMessageEncoder());

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