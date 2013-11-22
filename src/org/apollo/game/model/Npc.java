package org.apollo.game.model;

import org.apollo.game.event.Event;
import org.apollo.game.model.def.NpcDefinition;

/**
 * An {@link Npc} is a {@link Mob} that is not being controlled by a player.
 * 
 * @author Major
 */
public class Npc extends Mob {

	/**
	 * This npc's definition.
	 */
	private final NpcDefinition definition;

	/**
	 * Creates a new npc with the specified id and {@link Position}.
	 * 
	 * @param id The id.
	 * @param position The position.
	 */
	public Npc(int id, Position position) {
		this(NpcDefinition.lookup(id), position);
	}

	/**
	 * Creates a new npc with the specified {@link NpcDefinition} and {@link Position}.
	 * 
	 * @param definition The definition.
	 * @param position The position.
	 */
	public Npc(NpcDefinition definition, Position position) {
		super(position);
		this.definition = definition;
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.NPC;
	}

	/**
	 * Gets this npc's {@link NpcDefinition}
	 * 
	 * @return The definition.
	 */
	@Override
	public NpcDefinition getNpcDefinition() {
		return definition;
	}

	@Override
	public void send(Event event) {
	}

}