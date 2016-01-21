package org.apollo.game.model.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Tests the {@link MobRepository} class.
 * 
 * @author Ryley
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Player.class)
public final class MobRepositoryTest {

	/**
	 * The capacity of the MobRepository.
	 */
	private static final int CAPACITY = 10;

	/**
	 * Tests {@link MobRepository#capacity()}.
	 */
	@Test
	public void testCapacity() {
		MobRepository<Player> players = new MobRepository<>(CAPACITY);

		assertEquals(CAPACITY, players.capacity());
	}

	/**
	 * Tests {@link MobRepository#add(Mob)}.
	 */
	@Test
	public void testAdd() {
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
	public void testRemove() {
		MobRepository<Player> players = new MobRepository<>(CAPACITY);

		Player player = mock(Player.class);
		when(player.getIndex()).thenReturn(1);

		players.add(player);

		// Ensure validity of added Player otherwise this test cannot continue
		assertEquals(1, players.size());
		assertEquals(player, players.get(player.getIndex()));

		players.remove(player);
		assertEquals(0, players.size());
	}

	/**
	 * Ensures that a MobRepository maintains a fixed capacity.
	 */
	@Test
	public void testCapacityExceeded() {
		MobRepository<Player> players = new MobRepository<>(CAPACITY);

		// Fill up the repository
		for (int index = 0; index < CAPACITY; index++) {
			Player player = mock(Player.class);
			when(player.getIndex()).thenReturn(index + 1);

			assertTrue(players.add(player));
		}

		// Try to add one more Player.
		Player player = mock(Player.class);
		when(player.getIndex()).thenReturn(CAPACITY);

		assertTrue(players.full());
		assertFalse(players.add(player));
	}

	/**
	 * Tests {@link Iterator#hasNext()} for a MobRepository.
	 */
	@Test
	public void testIteratorHasNext() {
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
	public void testIteratorNext() {
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
	public void testIteratorRemove() {
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
	public void testIteratorNextOverflow() {
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

		// There should only be two elements in the iterator, this is invalid.
		iterator.next();
	}

	/**
	 * Ensures {@link Iterator#remove()} for a MobRepository throws an
	 * {@link IllegalStateException} if remove() is called without a call to
	 * next().
	 */
	@Test(expected = IllegalStateException.class)
	public void testIteratorRemoveIllegalState() {
		MobRepository<Player> players = new MobRepository<>(CAPACITY);

		Player player = mock(Player.class);
		when(player.getIndex()).thenReturn(1);

		players.add(player);

		Iterator<Player> iterator = players.iterator();

		// remove() may only be called once per call to next(), we have not
		// called next()
		iterator.remove();
	}

}
