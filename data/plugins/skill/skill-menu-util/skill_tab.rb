java_import 'org.apollo.game.message.impl.UpdateItemsMessage'
java_import 'org.apollo.game.message.impl.SetWidgetTextMessage'
java_import 'org.apollo.cache.def.ItemDefinition'
java_import 'org.apollo.game.model.Item'

class SkillTab
  attr_reader :title, :levels, :items
  
  def initialize(title, &block)
    @title =  title
    @levels = []
    @items = []

    self.instance_eval(&block) if block_given?
  end

  # Register an item to the skill menu
  # hashes: id/name, level
  def register_item(hash)
    id = hash[:id] || lookup_item(hash[:name])

    levels.push( hash[:level] )
    items.push( Item.new(id, 1))
  end

  # Display the tab
  def display_tab(player)
    player.send(UpdateItemsMessage.new(SKILL_MENU_ITEM_GROUP, items))

    player.send(SetWidgetTextMessage.new(SKILL_MENU_CURRENT_TAB, "#{title}"))

    for n in 0..ITEM_LIMIT
      player.send(SetWidgetTextMessage.new(SKILL_MENU_LEVEL_CHILD_START + n, "#{levels[n] || ""}"))
      player.send(SetWidgetTextMessage.new(SKILL_MENU_ITEM_CHILD_START + n, "#{items[n].nil? ? "" : ItemDefinition.lookup(items[n].id).name}"))
    end
  end
end
