package org.apollo.game.model.entity;

import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.net.codec.game.GamePacketBuilder;

/**
 * @author Khaled Abdeljaber
 */
public class PlayerUpdateInfo {

	public static final int NEW_PLAYERS_PER_CYCLE = 40;
	public static final int MAXIMUM_LOCAL_PLAYERS = 0xFF;

	/**
	 * <p>Activity flags are used to determine whether the player was updated this cycle, or skipped entirely due
	 * to inactivity. The method is used to group together inactive & active players respectively, allowing the
	 * server to skip larger amounts of players due to probability, effectively saving bandwidth in the long run,
	 * as every skipping call writes a number of bits on its own.</p>
	 */
	public byte[] activityFlags;

	/**
	 * <p>The indexes of the players currently in our viewport.</p>
	 */
	public int[] localIndexes;
	/**
	 * <p>The indexes of the players currently outside our viewport.</p>
	 */
	public int[] externalIndexes;
	/**
	 * <p>The players currently in our viewport.</p>
	 */
	public Player[] localPlayers;

	/**
	 * <p>The amount of players currently outside our viewport.</p>
	 */
	public int externalIndexesCount;
	/**
	 * <p>The amount of players currently inside our viewport.</p>
	 */
	public int localIndexesCount;

	/**
	 * <p>The secondary per-player basis buffer for masks.</p>
	 */
	public GamePacketBuilder smallMaskBuffer;
	/**
	 * <p>The primary buffer for masks.</p>
	 */
	public GamePacketBuilder largeMaskBuffer;
	/**
	 * <p>The primary buffer of GPI.</p>
	 */
	public GamePacketBuilder buffer;

	/**
	 * Whether or not the viewport is limited to 255 players.
	 */
	public boolean limitedMode;

	public PlayerUpdateInfo() {
		activityFlags = new byte[2048];
		activityFlags = new byte[2048];
		localPlayers = new Player[2048];
		localIndexes = new int[2048];
		externalIndexes = new int[2048];
		smallMaskBuffer = new GamePacketBuilder();
		largeMaskBuffer = new GamePacketBuilder();
		buffer = new GamePacketBuilder();
	}

	public void init(MobRepository<Player> repo, int index, Position position, GamePacketBuilder builder) {
		builder.switchToBitAccess();

		final var player = repo.get(index);
		builder.putBits(30, position.hashCode());
		localPlayers[player.getIndex()] = player;
		localIndexes[localIndexesCount++] = player.getIndex();
		for (var playerIndex = 1; playerIndex < 2048; playerIndex++) {
			if (playerIndex == player.getIndex()) {
				continue;
			}
			final var other = repo.get(playerIndex);
			builder.putBits(18, other == null ? 0 : other.getPosition().get18BitHash());
			externalIndexes[externalIndexesCount++] = playerIndex;
		}

		builder.switchToByteAccess();
	}
}
