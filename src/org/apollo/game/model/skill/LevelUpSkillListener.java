package org.apollo.game.model.skill;

import org.apollo.game.model.entity.Player;
import org.apollo.game.model.entity.Skill;
import org.apollo.game.model.entity.SkillSet;
import org.apollo.util.LanguageUtil;

/**
 * A {@link SkillListener} which notifies the player when they have levelled up a skill.
 *
 * @author Graham
 */
public final class LevelUpSkillListener extends SkillAdapter {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates the level up listener for the specified player.
	 *
	 * @param player The player.
	 */
	public LevelUpSkillListener(Player player) {
		this.player = player;
	}

	@Override
	public void levelledUp(SkillSet set, int id, Skill skill) {
		// TODO show the interface
		String name = Skill.getName(id);
		String article = LanguageUtil.getIndefiniteArticle(name);
		player.sendMessage("You've just advanced " + article + " " + name + " level! You have reached level " + skill.getMaximumLevel() + ".");

		if (Skill.isCombatSkill(id)) {
			player.getSkillSet().calculateCombatLevel();
		}
	}

}