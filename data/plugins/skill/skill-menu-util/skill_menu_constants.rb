java_import 'org.apollo.game.model.entity.Skill'

TAB_SIZE = 13
ITEM_LIMIT = 39

# Menu
SKILL_MENU_PARENT_ID = 8714
SKILL_MENU_TITLE_ID = 8716

# Tab
SKILL_MENU_ITEM_GROUP = 8847
SKILL_MENU_CURRENT_TAB = 8849
SKILL_MENU_LEVEL_CHILD_START = 8720
SKILL_MENU_ITEM_CHILD_START = 8760

# Map of the buttons to their skills
BUTTON_TO_SKILL = {
  8654 => Skill::ATTACK,
  8655 => Skill::HITPOINTS,
  8656 => Skill::MINING,
  8657 => Skill::STRENGTH,
  8658 => Skill::AGILITY,
  8659 => Skill::SMITHING,
  8660 => Skill::DEFENCE,
  8661 => Skill::HERBLORE,
  8662 => Skill::FISHING,
  8663 => Skill::RANGED,
  8664 => Skill::THIEVING,
  8665 => Skill::COOKING,
  8666 => Skill::PRAYER,
  8667 => Skill::CRAFTING,
  8668 => Skill::FIREMAKING,
  8669 => Skill::MAGIC,
  8670 => Skill::FLETCHING,
  8671 => Skill::WOODCUTTING,
  8672 => Skill::RUNECRAFT,
  12162 => Skill::SLAYER,
  13928 => Skill::FARMING
}

# The tab button/text ids in order.
MENU_TAB_IDS = [
  8846,
  8823,
  8824,
  8827,
  8837,
  8840,
  8843,
  8859,
  8862,
  8865,
  15303,
  15306,
  15309
]
