package org.apollo.game.sync.block;

import org.apollo.game.sync.block.SynchronizationBlock;

import java.util.EnumMap;

/**
 * @author Khaled Abdeljaber
 */
public class TitleBlock extends SynchronizationBlock {

	public enum TitlePosition {
		NAME_PREFIX, NAME_SUFFIX, COMBAT_SUFFIX
	}

	public static class Title {
		private String text;
		private TitlePosition position;

		public Title(String text, TitlePosition position) {
			this.text = text;
			this.position = position;
		}
	}

	private final EnumMap<TitlePosition, String> titles;

	TitleBlock(Title single, Title... others) {
		this.titles = new EnumMap<>(TitlePosition.class);
		titles.put(single.position, single.text);

		for (var title : others) {
			this.titles.put(title.position, title.text);
		}
	}

	public EnumMap<TitlePosition, String> getTitles() {
		return titles;
	}
}
