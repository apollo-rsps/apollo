package org.apollo.game.model.entity;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import org.apollo.game.message.impl.ConfigMessage;
import org.apollo.game.message.impl.IdAssignmentMessage;
import org.apollo.game.message.impl.IgnoreListMessage;
import org.apollo.game.message.impl.LogoutMessage;
import org.apollo.game.message.impl.SendFriendMessage;
import org.apollo.game.message.impl.ServerChatMessage;
import org.apollo.game.message.impl.SetWidgetTextMessage;
import org.apollo.game.message.impl.SwitchTabInterfaceMessage;
import org.apollo.game.message.impl.UpdateRunEnergyMessage;
import org.apollo.game.model.Appearance;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.WorldConstants;
import org.apollo.game.model.entity.attr.Attribute;
import org.apollo.game.model.entity.attr.AttributeDefinition;
import org.apollo.game.model.entity.attr.AttributeMap;
import org.apollo.game.model.entity.attr.AttributePersistence;
import org.apollo.game.model.entity.attr.NumericalAttribute;
import org.apollo.game.model.entity.attr.BooleanAttribute;
import org.apollo.game.model.entity.obj.DynamicGameObject;
import org.apollo.game.model.entity.setting.MembershipStatus;
import org.apollo.game.model.entity.setting.PrivacyState;
import org.apollo.game.model.entity.setting.PrivilegeLevel;
import org.apollo.game.model.entity.setting.ScreenBrightness;
import org.apollo.game.model.event.impl.LoginEvent;
import org.apollo.game.model.event.impl.LogoutEvent;
import org.apollo.game.model.inter.InterfaceConstants;
import org.apollo.game.model.inter.InterfaceListener;
import org.apollo.game.model.inter.InterfaceSet;
import org.apollo.game.model.inter.bank.BankConstants;
import org.apollo.game.model.inter.bank.BankInterfaceListener;
import org.apollo.game.model.inv.AppearanceInventoryListener;
import org.apollo.game.model.inv.FullInventoryListener;
import org.apollo.game.model.inv.Inventory;
import org.apollo.game.model.inv.Inventory.StackMode;
import org.apollo.game.model.inv.InventoryConstants;
import org.apollo.game.model.inv.InventoryListener;
import org.apollo.game.model.inv.SynchronizationInventoryListener;
import org.apollo.game.model.skill.LevelUpSkillListener;
import org.apollo.game.model.skill.SynchronizationSkillListener;
import org.apollo.game.session.GameSession;
import org.apollo.game.sync.block.SynchronizationBlock;
import org.apollo.net.message.Message;
import org.apollo.util.CollectionUtil;
import org.apollo.util.security.PlayerCredentials;

/**
 * A {@link Mob} that a user is controlling.
 *
 * @author Graham
 * @author Major
 */
public final class Player extends Mob {

	/**
	 * The {@code Mob}s current set of animations used in appearance updates.
	 */
	private AnimationMap animations = AnimationMap.DEFAULT_ANIMATION_SET;

	/**
	 * The default viewing distance, in tiles.
	 */
	private static final int DEFAULT_VIEWING_DISTANCE = 15;

	/**
	 * The current amount of appearance tickets.
	 */
	private static final AtomicInteger appearanceTicketCounter = new AtomicInteger(0);

	static {
		// TODO this should be a time rather than a flag
		AttributeMap.define("muted", AttributeDefinition.forBoolean(false, AttributePersistence.PERSISTENT));

		AttributeMap.define("banned", AttributeDefinition.forBoolean(false, AttributePersistence.PERSISTENT));
		AttributeMap.define("run_energy", AttributeDefinition.forInt(100, AttributePersistence.PERSISTENT));
	}

	/**
	 * Generates the next appearance ticket.
	 *
	 * @return The next available appearance ticket.
	 */
	private static int nextAppearanceTicket() {
		if (appearanceTicketCounter.incrementAndGet() == 0) {
			appearanceTicketCounter.set(1);
		}
		return appearanceTicketCounter.get();
	}

	/**
	 * This appearance tickets for this Player.
	 */
	private final int[] appearanceTickets = new int[WorldConstants.MAXIMUM_PLAYERS];

	/**
	 * This player's bank.
	 */
	private final Inventory bank = new Inventory(InventoryConstants.BANK_CAPACITY, StackMode.STACK_ALWAYS);

	/**
	 * This player's credentials.
	 */
	private final PlayerCredentials credentials;

	/**
	 * This player's interface set.
	 */
	private final InterfaceSet interfaceSet = new InterfaceSet(this);

	/**
	 * The Set of DynamicGameObjects that are visible to this Player.
	 */
	private final Set<DynamicGameObject> localObjects = new HashSet<>();

	/**
	 * A temporary queue of messages sent during the login process.
	 */
	private final Deque<Message> queuedMessages = new ArrayDeque<>();

	/**
	 * The player's appearance.
	 */
	private Appearance appearance = Appearance.DEFAULT_APPEARANCE;

	/**
	 * This Players appearance ticket.
	 */
	private int appearanceTicket = nextAppearanceTicket();

	/**
	 * The privacy state of this player's public chat.
	 */
	private PrivacyState chatPrivacy = PrivacyState.ON;

	/**
	 * A flag which indicates there are npcs that couldn't be added.
	 */
	private boolean excessiveNpcs;

	/**
	 * A flag which indicates there are players that couldn't be added.
	 */
	private boolean excessivePlayers;

	/**
	 * The privacy state of this player's private chat.
	 */
	private PrivacyState friendPrivacy = PrivacyState.ON;

	/**
	 * The list of usernames of players this player has befriended.
	 */
	private List<String> friends = new ArrayList<>();

	/**
	 * The list of usernames of players this player has ignored.
	 */
	private List<String> ignores = new ArrayList<>();

	/**
	 * Whether or not the player is skulled.
	 */
	private boolean isSkulled;

	/**
	 * The centre of the last region the client has loaded.
	 */
	private Position lastKnownRegion;

	/**
	 * The MembershipStatus of this Player.
	 */
	private MembershipStatus members = MembershipStatus.FREE;

	/**
	 * This player's prayer icon.
	 */
	private int prayerIcon = -1;

	/**
	 * The privilege level.
	 */
	private PrivilegeLevel privilegeLevel = PrivilegeLevel.STANDARD;

	/**
	 * A flag indicating if the region changed in the last cycle.
	 */
	private boolean regionChanged;

	/**
	 * A flag indicating if this player is running.
	 */
	private boolean running;

	/**
	 * The brightness of this player's screen.
	 */
	private ScreenBrightness screenBrightness = ScreenBrightness.NORMAL;

	/**
	 * The {@link GameSession} currently attached to this Player.
	 */
	private GameSession session;

	/**
	 * The privacy state of this player's trade chat.
	 */
	private PrivacyState tradePrivacy = PrivacyState.ON;

	/**
	 * The current maximum viewing distance of this Player.
	 */
	private int viewingDistance = DEFAULT_VIEWING_DISTANCE;

	/**
	 * A flag indicating if the player is withdrawing items as notes.
	 */
	private boolean withdrawingNotes;

	/**
	 * The world id of this Player.
	 */
	private int worldId = 1;

	/**
	 * Creates the Player.
	 *
	 * @param world The {@link World} containing the Player.
	 * @param credentials The player's credentials.
	 * @param position The initial position.
	 */
	public Player(World world, PlayerCredentials credentials, Position position) {
		super(world, position);
		this.credentials = credentials;

		init();
	}

	/**
	 * Adds the specified username to this player's friend list.
	 *
	 * @param username The username.
	 */
	public void addFriend(String username) {
		friends.add(username.toLowerCase());
	}

	/**
	 * Adds the specified username to this player's ignore list.
	 *
	 * @param username The username.
	 */
	public void addIgnore(String username) {
		ignores.add(username.toLowerCase());
	}

	/**
	 * Adds the specified {@link DynamicGameObject} to this Player's {@link Set}
	 * of visible objects.
	 *
	 * @param object The DynamicGameObject.
	 */
	public void addObject(DynamicGameObject object) {
		localObjects.add(object);
		object.addTo(this);
	}

	/**
	 * Decrements this player's viewing distance if it is greater than 1.
	 */
	public void decrementViewingDistance() {
		if (viewingDistance > 1) {
			viewingDistance--;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Player) {
			Player other = (Player) obj;
			return credentials.equals(other.credentials);
		}

		return false;
	}

	/**
	 * Sets the excessive npcs flag.
	 */
	public void flagExcessiveNpcs() {
		excessiveNpcs = true;
	}

	/**
	 * Sets the excessive players flag.
	 */
	public void flagExcessivePlayers() {
		excessivePlayers = true;
	}

	/**
	 * Indicates whether this player is friends with the player with the
	 * specified username or not.
	 *
	 * @param username The username of the other player.
	 * @return {@code true} if the specified username is on this player's friend
	 * list, otherwise {@code false}.
	 */
	public boolean friendsWith(String username) {
		return friends.contains(username.toLowerCase());
	}

	/**
	 * Gets the current set of animations used for character movement.
	 *
	 * @return The animation set.
	 */
	public final AnimationMap getAnimations() {
		return animations;
	}

	/**
	 * Gets the player's appearance.
	 *
	 * @return The appearance.
	 */
	public Appearance getAppearance() {
		return appearance;
	}

	/**
	 * Gets this Players appearance ticket.
	 *
	 * @return This Players appearance ticket.
	 */
	public int getAppearanceTicket() {
		return appearanceTicket;
	}

	/**
	 * Gets all of this Players appearance tickets.
	 *
	 * @return All of this Players appearance tickets.
	 */
	public int[] getAppearanceTickets() {
		return appearanceTickets;
	}

	/**
	 * Gets the mob's bank.
	 *
	 * @return The bank.
	 */
	public Inventory getBank() {
		return bank;
	}

	/**
	 * Gets this player's public chat privacy state.
	 *
	 * @return The privacy state.
	 */
	public PrivacyState getChatPrivacy() {
		return chatPrivacy;
	}

	/**
	 * Gets the player's credentials.
	 *
	 * @return The player's credentials.
	 */
	public PlayerCredentials getCredentials() {
		return credentials;
	}

	/**
	 * Gets the player's name, encoded as a long.
	 *
	 * @return The encoded player name.
	 */
	public long getEncodedName() {
		return credentials.getEncodedUsername();
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.PLAYER;
	}

	/**
	 * Gets this player's friend chat {@link PrivacyState}.
	 *
	 * @return The privacy state.
	 */
	public PrivacyState getFriendPrivacy() {
		return friendPrivacy;
	}

	/**
	 * Gets the {@link List} of this player's friends.
	 *
	 * @return The list.
	 */
	public List<String> getFriendUsernames() {
		return friends;
	}

	/**
	 * Gets the {@link List} of usernames of ignored players.
	 *
	 * @return The list.
	 */
	public List<String> getIgnoredUsernames() {
		return ignores;
	}

	@Override
	public int getInteractionIndex() {
		return getIndex() | 0x8000; // TODO magic constant
	}

	/**
	 * Gets this player's interface set.
	 *
	 * @return The interface set for this player.
	 */
	public InterfaceSet getInterfaceSet() {
		return interfaceSet;
	}

	/**
	 * Gets the last known region.
	 *
	 * @return The last known region, or {@code null} if the player has never
	 * known a region.
	 */
	public Position getLastKnownRegion() {
		return lastKnownRegion;
	}

	/**
	 * Gets the {@link MembershipStatus} of this Player.
	 *
	 * @return The MembershipStatus.
	 */
	public MembershipStatus getMembershipStatus() {
		return members;
	}

	/**
	 * Gets the player's prayer icon.
	 *
	 * @return The prayer icon.
	 */
	public int getPrayerIcon() {
		return prayerIcon;
	}

	/**
	 * Gets the privilege level.
	 *
	 * @return The privilege level.
	 */
	public PrivilegeLevel getPrivilegeLevel() {
		return privilegeLevel;
	}

	/**
	 * Gets the player's run energy.
	 *
	 * @return The run energy.
	 */
	public int getRunEnergy() {
		Attribute<Integer> energy = attributes.get("run_energy");
		return energy.getValue();
	}

	/**
	 * Gets this player's {@link ScreenBrightness}.
	 *
	 * @return The screen brightness.
	 */
	public ScreenBrightness getScreenBrightness() {
		return screenBrightness;
	}

	/**
	 * Gets the game session.
	 *
	 * @return The game session.
	 */
	public GameSession getSession() {
		return session;
	}

	/**
	 * Gets this player's trade {@link PrivacyState}.
	 *
	 * @return The privacy state.
	 */
	public PrivacyState getTradePrivacy() {
		return tradePrivacy;
	}

	/**
	 * Gets this player's name.
	 *
	 * @return The name.
	 */
	public String getUsername() {
		return credentials.getUsername();
	}

	/**
	 * Gets this player's viewing distance.
	 *
	 * @return The viewing distance.
	 */
	public int getViewingDistance() {
		return viewingDistance;
	}

	/**
	 * Gets the id of the world this player is in.
	 *
	 * @return The id.
	 */
	public int getWorldId() {
		return worldId;
	}

	/**
	 * Indicates whether or not the player with the specified username is on
	 * this player's ignore list.
	 *
	 * @param username The username of the player.
	 * @return {@code true} if the player is ignored, {@code false} if not.
	 */
	public boolean hasIgnored(String username) {
		return ignores.contains(username.toLowerCase());
	}

	/**
	 * Checks if this player has ever known a region.
	 *
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean hasLastKnownRegion() {
		return lastKnownRegion != null;
	}

	/**
	 * Checks if the region has changed.
	 *
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean hasRegionChanged() {
		return regionChanged;
	}

	@Override
	public int hashCode() {
		return credentials.hashCode();
	}

	/**
	 * Increments this player's viewing distance if it is less than the maximum
	 * viewing distance.
	 */
	public void incrementViewingDistance() {
		if (viewingDistance < Position.MAX_DISTANCE) {
			viewingDistance++;
		}
	}

	/**
	 * Returns if this player is banned or not.
	 */
	public boolean isBanned() {
		Attribute<Boolean> banned = attributes.get("banned");
		return banned.getValue();
	}

	/**
	 * Checks if there are excessive npcs.
	 *
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isExcessiveNpcsSet() {
		return excessiveNpcs;
	}

	/**
	 * Checks if there are excessive players.
	 *
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isExcessivePlayersSet() {
		return excessivePlayers;
	}

	/**
	 * Checks if this player has membership.
	 *
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isMembers() {
		return members == MembershipStatus.PAID;
	}

	/**
	 * Returns if this player is muted or not.
	 */
	public boolean isMuted() {
		Attribute<Boolean> muted = attributes.get("muted");
		return muted.getValue();
	}

	/**
	 * Checks if this player is running.
	 *
	 * @return {@code true} if the player is running, otherwise {@code false}.
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Indicates whether or not the player is skulled
	 *
	 * @return {@code true} if the player is skulled, otherwise {@code false}.
	 */
	public boolean isSkulled() {
		return isSkulled;
	}

	/**
	 * Checks if this player is withdrawing noted items.
	 *
	 * @return {@code true} if the player is currently withdrawing notes,
	 * otherwise {@code false}.
	 */
	public boolean isWithdrawingNotes() {
		return withdrawingNotes;
	}

	/**
	 * Logs the player out, if possible.
	 */
	public void logout() {
		if (world.submit(new LogoutEvent(this))) {
			send(new LogoutMessage());
		}

		localObjects.forEach(object -> object.removeFrom(this));
	}

	/**
	 * Opens this player's bank.
	 */
	public void openBank() {
		InventoryListener invListener = new SynchronizationInventoryListener(this, BankConstants.SIDEBAR_INVENTORY_ID);
		InventoryListener bankListener = new SynchronizationInventoryListener(this, BankConstants.BANK_INVENTORY_ID);

		inventory.addListener(invListener);
		bank.addListener(bankListener);
		inventory.forceRefresh();
		bank.forceRefresh();

		InterfaceListener interListener = new BankInterfaceListener(this, invListener, bankListener);
		interfaceSet.openWindowWithSidebar(interListener, BankConstants.BANK_WINDOW_ID, BankConstants.SIDEBAR_ID);
	}

	/**
	 * Removes the specified username from this player's friend list.
	 *
	 * @param username The username.
	 * @return {@code true} if the player's friend list contained the specified
	 * user, {@code false} if not.
	 */
	public boolean removeFriend(String username) {
		return friends.remove(username.toLowerCase());
	}

	/**
	 * Removes the specified username from this player's ignore list.
	 *
	 * @param username The username.
	 * @return {@code true} if the player's ignore list contained the specified
	 * user, {@code false} if not.
	 */
	public boolean removeIgnore(String username) {
		return ignores.remove(username.toLowerCase());
	}

	/**
	 * Removes the specified {@link DynamicGameObject} from this Player's
	 * {@link Set} of visible objects.
	 *
	 * @param object The DynamicGameObject.
	 */
	public void removeObject(DynamicGameObject object) {
		localObjects.remove(object);
		object.removeFrom(this);
	}

	/**
	 * Resets the excessive players flag.
	 */
	public void resetExcessivePlayers() {
		excessivePlayers = false;
	}

	/**
	 * Resets this player's viewing distance.
	 */
	public void resetViewingDistance() {
		viewingDistance = DEFAULT_VIEWING_DISTANCE;
	}

	/**
	 * Sends a {@link Message} to this player.
	 *
	 * @param message The message..
	 */
	public void send(Message message) {
		if (!isActive()) {
			queuedMessages.add(message);
			return;
		}

		if (!queuedMessages.isEmpty()) {
			CollectionUtil.pollAll(queuedMessages, session::dispatchMessage);
		}

		session.dispatchMessage(message);
	}

	/**
	 * Sends the initial messages.
	 */
	public void sendInitialMessages() {
		updateAppearance();
		send(new IdAssignmentMessage(index, members));
		sendMessage("Welcome to RuneScape.");

		if (isMuted()) {
			sendMessage("You are currently muted. Other players will not see your chat messages.");
		}

		int[] tabs = InterfaceConstants.DEFAULT_INVENTORY_TABS;
		for (int tab = 0; tab < tabs.length; tab++) {
			send(new SwitchTabInterfaceMessage(tab, tabs[tab]));
		}

		inventory.forceRefresh();
		equipment.forceRefresh();
		bank.forceRefresh();
		skillSet.forceRefresh();

		world.submit(new LoginEvent(this));
	}

	/**
	 * Sends the specified chat message to the player.
	 *
	 * @param message The message to send.
	 */
	public void sendMessage(String message) {
		send(new ServerChatMessage(message));
	}

	/**
	 * Sends the quest interface
	 *
	 * @param text The text to display on the interface.
	 */
	public void sendQuestInterface(List<String> text) {
		int size = text.size(), lines = InterfaceConstants.QUEST_TEXT.length;
		Preconditions.checkArgument(size <= lines, "List contains too much text to display on this interface.");

		for (int pos = 0; pos < lines; pos++) {
			send(new SetWidgetTextMessage(InterfaceConstants.QUEST_TEXT[pos], pos < size ? text.get(pos) : ""));
		}
		interfaceSet.openWindow(InterfaceConstants.QUEST_INTERFACE);
	}

	/**
	 * Sends the friend and ignore user lists.
	 */
	public void sendUserLists() {
		if (!ignores.isEmpty()) {
			send(new IgnoreListMessage(ignores));
		}

		for (String username : friends) {
			int worldId = world.isPlayerOnline(username) ? world.getPlayer(username).worldId : 0;
			send(new SendFriendMessage(username, worldId));
		}
	}

	/**
	 * Sets the set of animations to use in {@code Mob} appearance updates.
	 *
	 * @param animations The set of animations to use.
	 */
	public final void setAnimations(AnimationMap animations) {
		this.animations = animations;
	}

	/**
	 * Sets the player's appearance.
	 *
	 * @param appearance The new appearance.
	 */
	public void setAppearance(Appearance appearance) {
		this.appearance = appearance;
		updateAppearance();
	}

	/**
	 * Sets the chat {@link PrivacyState}.
	 *
	 * @param chatPrivacy The privacy state.
	 */
	public void setChatPrivacy(PrivacyState chatPrivacy) {
		this.chatPrivacy = chatPrivacy;
	}

	/**
	 * Sets the friend {@link PrivacyState}.
	 *
	 * @param friendPrivacy The privacy state.
	 */
	public void setFriendPrivacy(PrivacyState friendPrivacy) {
		this.friendPrivacy = friendPrivacy;
	}

	/**
	 * Sets the {@link List} of this player's friends.
	 *
	 * @param friends The friends.
	 */
	public void setFriendUsernames(List<String> friends) {
		this.friends = friends;
	}

	/**
	 * Sets the {@link List} of this player's ignored players.
	 *
	 * @param ignores The ignored player list.
	 */
	public void setIgnoredUsernames(List<String> ignores) {
		this.ignores = ignores;
	}

	/**
	 * Sets the last known region.
	 *
	 * @param lastKnownRegion The last known region.
	 */
	public void setLastKnownRegion(Position lastKnownRegion) {
		this.lastKnownRegion = lastKnownRegion;
	}

	/**
	 * Changes the membership status of this player.
	 *
	 * @param members The new membership flag.
	 */
	public void setMembers(MembershipStatus members) {
		this.members = members;
	}

	/**
	 * Sets the player's prayer icon. TODO make this an attribute?
	 *
	 * @param prayerIcon The prayer icon.
	 */
	public void setPrayerIcon(int prayerIcon) {
		this.prayerIcon = prayerIcon;
	}

	/**
	 * Sets the privilege level.
	 *
	 * @param privilegeLevel The privilege level.
	 */
	public void setPrivilegeLevel(PrivilegeLevel privilegeLevel) {
		this.privilegeLevel = privilegeLevel;
	}

	/**
	 * Sets the region changed flag.
	 *
	 * @param regionChanged Whether or not the region has changed.
	 */
	public void setRegionChanged(boolean regionChanged) {
		this.regionChanged = regionChanged;
	}

	/**
	 * Sets the player's run energy.
	 *
	 * @param energy The energy.
	 */
	public void setRunEnergy(int energy) {
		attributes.set("run_energy", new NumericalAttribute(energy));
		send(new UpdateRunEnergyMessage(energy));
	}

	/**
	 * Sets the {@link ScreenBrightness} of this player.
	 *
	 * @param brightness The screen brightness.
	 */
	public void setScreenBrightness(ScreenBrightness brightness) {
		screenBrightness = brightness;
	}

	/**
	 * Sets the player's {@link GameSession}.
	 *
	 * @param session The player's {@link GameSession}.
	 */
	public void setSession(GameSession session) {
		this.session = session;
	}

	/**
	 * Sets whether or not the player is skulled. TODO make this an attribute
	 *
	 * @param isSkulled Whether or not the player is skulled.
	 */
	public void setSkulled(boolean isSkulled) {
		this.isSkulled = isSkulled;
	}

	/**
	 * Sets the trade {@link PrivacyState}.
	 *
	 * @param tradePrivacy The privacy state.
	 */
	public void setTradePrivacy(PrivacyState tradePrivacy) {
		this.tradePrivacy = tradePrivacy;
	}

	/**
	 * Sets whether or not the player is withdrawing notes from the bank.
	 *
	 * @param withdrawingNotes Whether or not the player is withdrawing noted
	 * items.
	 */
	public void setWithdrawingNotes(boolean withdrawingNotes) {
		this.withdrawingNotes = withdrawingNotes;
	}

	/**
	 * Ban the player.
	 */
	public void ban() {
		attributes.set("banned", new BooleanAttribute(true));
	}

	/**
	 * Sets the mute status of a player.
	 *
	 * @param muted Whether the player is muted.
	 */
	public void setMuted(boolean muted) {
		attributes.set("muted", new BooleanAttribute(muted));
	}

	@Override
	public void shout(String message, boolean chatOnly) {
		blockSet.add(SynchronizationBlock.createForceChatBlock(chatOnly ? message : '~' + message));
	}

	@Override
	public void teleport(Position position) {
		super.teleport(position);

		if (interfaceSet.size() > 0) {
			interfaceSet.close();
		}
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("username", getUsername()).add("privilege", privilegeLevel)
			.toString();
	}

	/**
	 * Toggles the player's run status.
	 */
	public void toggleRunning() {
		running = !running;
		walkingQueue.setRunning(running);
		send(new ConfigMessage(173, running ? 1 : 0));
	}

	/**
	 * Updates the appearance for this Player.
	 */
	public void updateAppearance() {
		appearanceTicket = nextAppearanceTicket();
		blockSet.add(SynchronizationBlock.createAppearanceBlock(this));
	}

	/**
	 * Initialises this player.
	 */
	private void init() {
		initInventories();
		initSkills();
	}

	/**
	 * Initialises the player's inventories.
	 */
	private void initInventories() {
		InventoryListener fullInventory = new FullInventoryListener(this, FullInventoryListener.FULL_INVENTORY_MESSAGE);
		InventoryListener fullBank = new FullInventoryListener(this, FullInventoryListener.FULL_BANK_MESSAGE);
		InventoryListener appearance = new AppearanceInventoryListener(this);

		InventoryListener syncInventory = new SynchronizationInventoryListener(this,
			SynchronizationInventoryListener.INVENTORY_ID);
		InventoryListener syncBank = new SynchronizationInventoryListener(this, BankConstants.BANK_INVENTORY_ID);
		InventoryListener syncEquipment = new SynchronizationInventoryListener(this,
			SynchronizationInventoryListener.EQUIPMENT_ID);

		inventory.addListener(syncInventory);
		inventory.addListener(fullInventory);
		bank.addListener(syncBank);
		bank.addListener(fullBank);
		equipment.addListener(syncEquipment);
		equipment.addListener(appearance);
	}

	/**
	 * Initialises the player's skills.
	 */
	private void initSkills() {
		skillSet.addListener(new SynchronizationSkillListener(this));
		skillSet.addListener(new LevelUpSkillListener(this));
	}

}