package org.apollo.game.model.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Tests the {@link MobRepository} class.
 *
 * @author Ryley
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Player.class)
public final class MobRepositoryTests {

	/**
	 * The capacity of the MobRepository.
	 */
	private static final int CAPACITY = 10;

	/**
	 * Tests {@link MobRepository#capacity()}.
	 */
	@Test
	public void capacity() {
		MobRepository<Player> players = new MobRepository<>(CAPACITY);

		assertEquals(CAPACITY, players.capacity());
	}

	/**
	 * Tests {@link MobRepository#add(Mob)}.
	 */
	@Test
	public void add() {
		MobRepository<Player> players = new MobRepository<>(CAPACITY);

		Player player = mock(Player.class);
		when(player.getIndex()).thenReturn(1);

		players.add(player);

		assertEquals(1, players.size());
		assertEquals(player, players.get(player.getIndex()));
	}

	/**
	 * Tests {@link MobRepository#remove(Mob)}.
	 */
	@Test
	public void remove() {
		MobRepository<Player> players = new MobRepository<>(CAPACITY);

		Player player = mock(Player.class);
		when(player.getIndex()).thenReturn(1);

		players.add(player);

		assertEquals(1, players.size());
		assertEquals(player, players.get(player.getIndex()));

		players.remove(player);
		assertEquals(0, players.size());
	}

	/**
	 * Tests failing of {@link MobRepository#remove(Mob)}
	 */
	@Test(expected = NullPointerException.class)
	public void removeNull() {
		MobRepository<Player> players = new MobRepository<>(CAPACITY);
		players.remove(null);
	}

	/**
	 * Ensures that a MobRepository maintains a fixed capacity.
	 */
	@Test
	public void capacityExceeded() {
		MobRepository<Player> players = new MobRepository<>(CAPACITY);

		for (int index = 0; index < CAPACITY; index++) {
			Player player = mock(Player.class);
			when(player.getIndex()).thenReturn(index + 1);

			assertTrue(players.add(player));
		}

		Player player = mock(Player.class);
		when(player.getIndex()).thenReturn(CAPACITY);

		assertTrue(players.full());
		assertFalse(players.add(player));
	}

	/**
	 * Tests {@link Iterator#hasNext()} for a MobRepository.
	 */
	@Test
	public void iteratorHasNext() {
		MobRepository<Player> players = new MobRepository<>(CAPACITY);

		Player player = mock(Player.class);
		when(player.getIndex()).thenReturn(1);

		players.add(player);

		Iterator<Player> iterator = players.iterator();
		assertTrue(iterator.hasNext());
	}

	/**
	 * Tests {@link Iterator#next()} for a MobRepository.
	 */
	@Test
	public void iteratorNext() {
		MobRepository<Player> players = new MobRepository<>(CAPACITY);

		Player first = mock(Player.class);
		when(first.getIndex()).thenReturn(1);

		Player second = mock(Player.class);
		when(second.getIndex()).thenReturn(2);

		players.add(first);
		players.add(second);

		Iterator<Player> iterator = players.iterator();

		assertTrue(iterator.hasNext());
		assertEquals(first, iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals(second, iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 * Tests {@link Iterator#remove()} for a MobRepository.
	 */
	@Test
	public void iteratorRemove() {
		MobRepository<Player> players = new MobRepository<>(CAPACITY);

		Player first = mock(Player.class);
		when(first.getIndex()).thenReturn(1);

		Player second = mock(Player.class);
		when(second.getIndex()).thenReturn(2);

		players.add(first);
		players.add(second);

		Iterator<Player> iterator = players.iterator();

		iterator.next();
		iterator.remove();
		iterator.next();
		iterator.remove();

		assertFalse(iterator.hasNext());
	}

	/**
	 * Ensures {@link Iterator#next()} for a MobRepository throws a
	 * {@link NoSuchElementException} if the iterator contains no more elements.
	 */
	@Test(expected = NoSuchElementException.class)
	public void iteratorNextOverflow() {
		MobRepository<Player> players = new MobRepository<>(CAPACITY);

		Player first = mock(Player.class);
		when(first.getIndex()).thenReturn(1);

		Player second = mock(Player.class);
		when(second.getIndex()).thenReturn(2);

		players.add(first);
		players.add(second);

		Iterator<Player> iterator = players.iterator();

		iterator.next();
		iterator.next();

		iterator.next();
	}

	/**
	 * Ensures {@link Iterator#remove()} for a MobRepository throws an
	 * {@link IllegalStateException} if remove() is called without a call to
	 * next().
	 */
	@Test(expected = IllegalStateException.class)
	public void iteratorRemoveIllegalState() {
		MobRepository<Player> players = new MobRepository<>(CAPACITY);

		Player player = mock(Player.class);
		when(player.getIndex()).thenReturn(1);

		players.add(player);

		Iterator<Player> iterator = players.iterator();

		iterator.remove();
	}

}
