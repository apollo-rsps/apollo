package org.apollo.game.model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import org.apollo.game.event.Event;
import org.apollo.game.event.impl.ConfigEvent;
import org.apollo.game.event.impl.IdAssignmentEvent;
import org.apollo.game.event.impl.LogoutEvent;
import org.apollo.game.event.impl.ServerMessageEvent;
import org.apollo.game.event.impl.SetWidgetTextEvent;
import org.apollo.game.event.impl.SwitchTabInterfaceEvent;
import org.apollo.game.event.impl.UpdateRunEnergyEvent;
import org.apollo.game.model.inter.InterfaceConstants;
import org.apollo.game.model.inter.InterfaceSet;
import org.apollo.game.model.inter.bank.BankConstants;
import org.apollo.game.model.inv.AppearanceInventoryListener;
import org.apollo.game.model.inv.FullInventoryListener;
import org.apollo.game.model.inv.InventoryListener;
import org.apollo.game.model.inv.SynchronizationInventoryListener;
import org.apollo.game.model.skill.LevelUpSkillListener;
import org.apollo.game.model.skill.SkillListener;
import org.apollo.game.model.skill.SynchronizationSkillListener;
import org.apollo.game.sync.block.SynchronizationBlock;
import org.apollo.net.session.GameSession;
import org.apollo.security.PlayerCredentials;
import org.apollo.util.Point;

/**
 * A {@link Player} is a {@link Character} that a user is controlling.
 * 
 * @author Graham
 */
public final class Player extends Character {

	/**
	 * An enumeration with the different privilege levels a player can have.
	 * 
	 * @author Graham
	 */
	public enum PrivilegeLevel {

		/**
		 * An administrator (rights 2) account.
		 */
		ADMINISTRATOR(2),

		/**
		 * A player moderator (rights 1) account.
		 */
		MODERATOR(1),

		/**
		 * A standard (rights 0) account.
		 */
		STANDARD(0);

		/**
		 * Gets the privilege level for the specified numerical level.
		 * 
		 * @param numericalLevel The numerical level.
		 * @return The privilege level.
		 * @throws IllegalArgumentException If the numerical level is invalid.
		 */
		public static PrivilegeLevel valueOf(int numericalLevel) {
			for (PrivilegeLevel level : values()) {
				if (level.numericalLevel == numericalLevel) {
					return level;
				}
			}
			throw new IllegalArgumentException("invalid numerical level");
		}

		/**
		 * The numerical level used in the protocol.
		 */
		private final int numericalLevel;

		/**
		 * Creates a privilege level.
		 * 
		 * @param numericalLevel The numerical level.
		 */
		private PrivilegeLevel(int numericalLevel) {
			this.numericalLevel = numericalLevel;
		}

		/**
		 * Gets the numerical level.
		 * 
		 * @return The numerical level used in the protocol.
		 */
		public int toInteger() {
			return numericalLevel;
		}

	}

	/**
	 * The player's appearance.
	 */
	private Appearance appearance = Appearance.DEFAULT_APPEARANCE;

	/**
	 * A {@link List} of this player's mouse clicks.
	 */
	private Deque<Point> clicks = new ArrayDeque<Point>();

	/**
	 * The player's credentials.
	 */
	private PlayerCredentials credentials;

	/**
	 * A flag indicating if the player has designed their character.
	 */
	private boolean designedCharacter = false;

	/**
	 * A flag which indicates there are npcs that couldn't be added.
	 */
	private boolean excessiveNpcs = false;

	/**
	 * A flag which indicates there are players that couldn't be added.
	 */
	private boolean excessivePlayers = false;

	/**
	 * This player's head icon.
	 */
	private int headIcon = -1;

	/**
	 * This player's interface set.
	 */
	private final InterfaceSet interfaceSet = new InterfaceSet(this);

	/**
	 * The centre of the last region the client has loaded.
	 */
	private Position lastKnownRegion;

	/**
	 * The membership flag.
	 */
	private boolean members = false;

	/**
	 * This player's prayer icon.
	 */
	private int prayerIcon = -1;

	/**
	 * The privilege level.
	 */
	private PrivilegeLevel privilegeLevel = PrivilegeLevel.STANDARD;

	/**
	 * A temporary queue of events sent during the login process.
	 */
	private final Deque<Event> queuedEvents = new ArrayDeque<Event>();

	/**
	 * A flag indicating if the region changed in the last cycle.
	 */
	private boolean regionChanged = false;

	/**
	 * The player's run energy.
	 */
	private int runEnergy = 100;

	/**
	 * A flag indicating if this player is running.
	 */
	private boolean running = false;

	/**
	 * The {@link GameSession} currently attached to this {@link Player}.
	 */
	private GameSession session;

	/**
	 * The current maximum viewing distance of this player.
	 */
	private int viewingDistance = 1;

	/**
	 * A flag indicating if the player is withdrawing items as notes.
	 */
	private boolean withdrawingNotes = false;

	/**
	 * Creates the {@link Player}.
	 * 
	 * @param credentials The player's credentials.
	 * @param position The initial position.
	 */
	public Player(PlayerCredentials credentials, Position position) {
		super(position);
		init();
		this.credentials = credentials;
	}

	/**
	 * Adds a click, represented by a {@link Point}, to the {@link List} of clicks.
	 * 
	 * @param point The point.
	 * @return {@code true} if the point was added successfully.
	 */
	public boolean addClick(Point point) {
		return clicks.add(point);
	}

	/**
	 * Decrements this player's viewing distance if it is greater than 1.
	 */
	public void decrementViewingDistance() {
		if (viewingDistance > 1) {
			viewingDistance--;
		}
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
	 * Gets the player's appearance.
	 * 
	 * @return The appearance.
	 */
	public Appearance getAppearance() {
		return appearance;
	}

	/**
	 * Gets the {@link Deque} of clicks.
	 * 
	 * @return The deque.
	 */
	public Deque<Point> getClicks() {
		return clicks;
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
	 * Gets the player's head icon.
	 * 
	 * @return The head icon.
	 */
	public int getHeadIcon() {
		return headIcon;
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
	 * Gets this player's last click, represented by a {@link Point}.
	 * 
	 * @return The click.
	 */
	public Point getLastClick() {
		return clicks.pollLast();
	}

	/**
	 * Gets the last known region.
	 * 
	 * @return The last known region, or {@code null} if the player has never known a region.
	 */
	public Position getLastKnownRegion() {
		return lastKnownRegion;
	}

	/**
	 * Gets the player's name.
	 * 
	 * @return The player's name.
	 */
	public String getName() {
		return credentials.getUsername();
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
		return runEnergy;
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
	 * Gets this player's viewing distance.
	 * 
	 * @return The viewing distance.
	 */
	public int getViewingDistance() {
		return viewingDistance;
	}

	/**
	 * Checks if the player has designed their character.
	 * 
	 * @return A flag indicating if the player has designed their character.
	 */
	public boolean hasDesignedCharacter() {
		return designedCharacter;
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

	/**
	 * Increments this player's viewing distance if it is less than the maximum viewing distance.
	 */
	public void incrementViewingDistance() {
		if (viewingDistance < Position.MAX_DISTANCE) {
			viewingDistance++;
		}
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
		Inventory inventory = getInventory();
		Inventory bank = getBank();
		Inventory equipment = getEquipment();

		// inventory full listeners
		InventoryListener fullInventoryListener = new FullInventoryListener(this,
				FullInventoryListener.FULL_INVENTORY_MESSAGE);
		InventoryListener fullBankListener = new FullInventoryListener(this, FullInventoryListener.FULL_BANK_MESSAGE);
		InventoryListener fullEquipmentListener = new FullInventoryListener(this,
				FullInventoryListener.FULL_EQUIPMENT_MESSAGE);

		// equipment appearance listener
		InventoryListener appearanceListener = new AppearanceInventoryListener(this);

		// synchronization listeners
		InventoryListener syncInventoryListener = new SynchronizationInventoryListener(this,
				SynchronizationInventoryListener.INVENTORY_ID);
		InventoryListener syncBankListener = new SynchronizationInventoryListener(this, BankConstants.BANK_INVENTORY_ID);
		InventoryListener syncEquipmentListener = new SynchronizationInventoryListener(this,
				SynchronizationInventoryListener.EQUIPMENT_ID);

		// add the listeners
		inventory.addListener(syncInventoryListener);
		inventory.addListener(fullInventoryListener);
		bank.addListener(syncBankListener);
		bank.addListener(fullBankListener);
		equipment.addListener(syncEquipmentListener);
		equipment.addListener(appearanceListener);
		equipment.addListener(fullEquipmentListener);
	}

	/**
	 * Initialises the player's skills.
	 */
	private void initSkills() {
		SkillSet skills = getSkillSet();

		// synchronization listener
		SkillListener syncListener = new SynchronizationSkillListener(this);

		// level up listener
		SkillListener levelUpListener = new LevelUpSkillListener(this);

		// add the listeners
		skills.addListener(syncListener);
		skills.addListener(levelUpListener);
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
	 * Checks if this player account has membership.
	 * 
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isMembers() {
		return members;
	}

	/**
	 * Gets whether the player is running or not.
	 * 
	 * @return {@code true} if the player is running, otherwise {@code false}.
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Gets the withdrawing notes flag.
	 * 
	 * @return The flag.
	 */
	public boolean isWithdrawingNotes() {
		return withdrawingNotes;
	}

	/**
	 * Logs the player out, if possible.
	 */
	public void logout() {
		send(new LogoutEvent());
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
		viewingDistance = 1;
	}

	@Override
	public void send(Event event) {
		if (isActive()) {
			if (!queuedEvents.isEmpty()) {
				for (Event queuedEvent : queuedEvents) {
					session.dispatchEvent(queuedEvent);
				}
				queuedEvents.clear();
			}
			session.dispatchEvent(event);
		} else {
			queuedEvents.add(event);
		}
	}

	/**
	 * Sends the initial events.
	 */
	private void sendInitialEvents() {
		send(new IdAssignmentEvent(getIndex(), members)); // TODO should this be sent when we reconnect?
		sendMessage("Welcome to RuneScape.");

		if (!designedCharacter) {
			interfaceSet.openWindow(InterfaceConstants.CHARACTER_DESIGN);
		}

		int[] tabs = InterfaceConstants.DEFAULT_INVENTORY_TABS;
		for (int i = 0; i < tabs.length; i++) {
			send(new SwitchTabInterfaceEvent(i, tabs[i]));
		}

		getInventory().forceRefresh();
		getEquipment().forceRefresh();
		getBank().forceRefresh();

		getSkillSet().forceRefresh();
	}

	/**
	 * Sends a message to the character.
	 * 
	 * @param message The message.
	 */
	public void sendMessage(String message) {
		send(new ServerMessageEvent(message));
	}

	/**
	 * Sends the quest interface
	 * 
	 * @param text The text to display on the interface.
	 */
	public void sendQuestInterface(List<String> text) {
		int size = text.size(), lines = InterfaceConstants.QUEST_TEXT.length;
		if (size > lines) {
			throw new IllegalArgumentException("list contains too much text for this interface.");
		}

		for (int pos = 0; pos < lines; pos++) {
			send(new SetWidgetTextEvent(InterfaceConstants.QUEST_TEXT[pos], pos < size ? text.get(pos) : ""));
		}
		interfaceSet.openWindow(InterfaceConstants.QUEST_INTERFACE);
	}

	/**
	 * Sets the player's appearance.
	 * 
	 * @param appearance The new appearance.
	 */
	public void setAppearance(Appearance appearance) {
		this.appearance = appearance;
		getBlockSet().add(SynchronizationBlock.createAppearanceBlock(this));
	}

	/**
	 * Sets the character design flag.
	 * 
	 * @param designedCharacter A flag indicating if the character has been designed.
	 */
	public void setDesignedCharacter(boolean designedCharacter) {
		this.designedCharacter = designedCharacter;
	}

	/**
	 * Sets the player's head icon.
	 * 
	 * @param headIcon The head icon.
	 */
	public void setHeadIcon(int headIcon) {
		this.headIcon = headIcon;
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
	public void setMembers(boolean members) {
		this.members = members;
	}

	/**
	 * Sets the player's prayer icon.
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
	 * @param regionChanged A flag indicating if the region has changed.
	 */
	public void setRegionChanged(boolean regionChanged) {
		this.regionChanged = regionChanged;
	}

	/**
	 * Sets the player's run energy.
	 * 
	 * @param runEnergy The energy.
	 */
	public void setRunEnergy(int runEnergy) {
		this.runEnergy = runEnergy;
		send(new UpdateRunEnergyEvent(runEnergy));
	}

	/**
	 * Sets the player's {@link GameSession}.
	 * 
	 * @param session The player's {@link GameSession}.
	 * @param reconnecting The reconnecting flag.
	 */
	public void setSession(GameSession session, boolean reconnecting) {
		this.session = session;
		if (!reconnecting) {
			sendInitialEvents();
		}
		getBlockSet().add(SynchronizationBlock.createAppearanceBlock(this));
	}

	/**
	 * Sets whether the player is withdrawing notes from the bank.
	 * 
	 * @param withdrawingNotes Whether the player is withdrawing noted items or not.
	 */
	public void setWithdrawingNotes(boolean withdrawingNotes) {
		this.withdrawingNotes = withdrawingNotes;
	}

	@Override
	public void teleport(Position position) {
		super.teleport(position); // TODO put this in the same place as Character#teleport and WalkEventHandler!!
		if (interfaceSet.size() > 0) {
			interfaceSet.close();
		}
	}

	/**
	 * Toggles whether the player is running or not.
	 */
	public void toggleRunning() {
		running = !running;
		getWalkingQueue().setRunningQueue(running);
		send(new ConfigEvent(173, running ? 1 : 0));
	}

	@Override
	public String toString() {
		return Player.class.getName() + " [username=" + credentials.getUsername() + ", privilegeLevel="
				+ privilegeLevel + "]";
	}

}