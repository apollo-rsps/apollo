java_import 'org.apollo.game.model.entity.Skill'
java_import 'org.apollo.game.message.impl.UpdateItemsMessage'
java_import 'org.apollo.game.message.impl.SetWidgetTextMessage'

SKILL_MENUS = {}

class SkillMenu
  attr_reader :skill, :tabs
  def initialize(skill, &block)
    @skill = skill
    @tabs = {}
    self.instance_eval(&block) if block_given?
  end

  # Create a new tab
  def tab(name, &block)
    tabs[name] = SkillTab.new(name, &block)
  end

  # Display the menu
  def display_menu(player, index)
    tab_data = tabs.values

    for n in 0..TAB_SIZE
      player.send(SetWidgetTextMessage.new(MENU_TAB_IDS[n], "#{tab_data[n].nil? ? "" : tab_data[n].title}"))
    end

    player.send(SetWidgetTextMessage.new(SKILL_MENU_TITLE_ID, "#{Skill::getName(skill)}"))
    player.interfaceSet.open_window(SKILL_MENU_PARENT_ID)
  end

  # Changes the current tab of this menu
  def change_tab(player, index)
    name = tabs.keys[index]
    tabs[name].display_tab(player) unless tabs[name].nil?
  end

  # Register an item to the skill menu
  # hashes: tab, id/name, level
  def register_item(hash)
    tab = tabs[hash[:tab]] || tab(hash[:tab])
    tab.register_item(hash)
  end
end

# Create a new skill menu. All the skills are in the sub folder skill-menus.
def create_menu(skill, &block)
  SKILL_MENUS[skill] = SkillMenu.new(skill, &block)
end

# Register an item to the skill menu
# hashes: tab, id/name, level
def register_item(skill, hash)
  SKILL_MENUS[skill].register_item(hash)
end