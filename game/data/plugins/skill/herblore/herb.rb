require 'java'

java_import 'org.apollo.game.action.Action'
java_import 'org.apollo.util.LanguageUtil'

# A herb is an ingredient that requires identification before being used.
class Herb < Ingredient
  include HerbloreMethod

  attr_reader :unidentified, :level, :experience

  def initialize(item_id, unidentified, level, experience)
    super item_id

    @unidentified = unidentified
    @level = level
    @experience = experience
  end

  def invoke(player, _id, slot)
    item = player.inventory.get(slot)
    player.start_action(HerbIdentificationAction.new(player, self, slot, item))
  end
end

# An action that makes a player identify a herb.
class HerbIdentificationAction < Action
  attr_reader :herb, :slot, :item, :pulses

  def initialize(player, herb, slot, item)
    super(0, true, player)

    @herb = herb
    @slot = slot
    @item = item
    @pulses = 0
  end

  def execute
    if @pulses == 0
      unless check_skill(mob, @herb.level, 'identify this herb')
        stop
        return
      end
    end

    execute_action
    @pulses += 1
  end

  def execute_action
    player = mob
    inventory = player.inventory

    if inventory.remove_slot(@slot, 1) == 1
      identified = @herb.item

      inventory.add(identified)

      article = LanguageUtil.getIndefiniteArticle(identified.definition.name)
      player.skill_set.add_experience(Skill::HERBLORE, @herb.experience)
      player.send_message("This herb is #{article} #{identified.definition.name}.")
    end

    stop
  end

  def equals(other)
    get_class == other.get_class && slot == other.slot && herb == other.herb
  end
end

# Appends a herb to the InventoryItemMessage interception.
def append_herb(item_id, unidentified, level, experience)
  herb = Herb.new(item_id, unidentified, level, experience)
  append_herblore_item(herb, unidentified)
  herb
end

# Herbs

GUAM_LEAF   = append_herb(249,  199,  1,  2.5)
MARRENTILL  = append_herb(251,  201,  5,  3.8)
TARROMIN    = append_herb(253,  203,  11, 5)
HARRALANDER = append_herb(255,  205,  20, 6.3)
RANARR      = append_herb(257,  207,  25, 7.5)
TOADFLAX    = append_herb(2998, 3049, 30, 8)
IRIT_LEAF   = append_herb(259,  209,  40, 8.8)
AVANTOE     = append_herb(261,  211,  48, 10)
KWUARM      = append_herb(263,  213,  54, 11.3)
SNAPDRAGON  = append_herb(3000, 3051, 59, 11.8)
CADANTINE   = append_herb(265,  215,  65, 12.5)
LANTADYME   = append_herb(2481, 2485, 67, 13.1)
DWARF_WEED  = append_herb(267,  217,  70, 13.8)
TORSTOL     = append_herb(269,  219,  75, 15)
