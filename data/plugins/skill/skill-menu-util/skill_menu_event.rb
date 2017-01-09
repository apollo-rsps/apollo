java_import 'org.apollo.game.model.entity.Skill'
java_import 'org.apollo.game.message.impl.UpdateItemsMessage'
java_import 'org.apollo.game.message.impl.SetWidgetTextMessage'

ACTIVE_SKILL_MENU = 0

on :message, :button do |player, message|
  id = message.widget_id

  skill = BUTTON_TO_SKILL[id]
  index = MENU_TAB_IDS.index(id)

  open_menu(player, skill) unless skill.nil?
  change_tab(player, index) unless index.nil?
end

# Open a skill menu
def open_menu(player, skill)
  unless SKILL_MENUS[skill].nil?
    $ACTIVE_SKILL_MENU = skill

    SKILL_MENUS[$ACTIVE_SKILL_MENU].display_menu(player, 0)
    change_tab(player, 0)
  end
end

# Change the tab of the current skill menu
def change_tab(player, index)
  SKILL_MENUS[$ACTIVE_SKILL_MENU].change_tab(player, index)
end
