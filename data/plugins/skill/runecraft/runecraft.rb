require 'java'

java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.Graphic'
java_import 'org.apollo.game.model.entity.Skill'

RUNECRAFTING_ANIMATION = Animation.new(791)
RUNECRAFTING_GRAPHIC = Graphic.new(186, 0, 100)

RUNE_ESSENCE_ID = 1436

# An action when the player crafts a rune.
class RunecraftingAction < DistancedAction
  attr_reader :player, :rune

  def initialize(player, rune, object_position)
    super(1, true, player, object_position, 3)
    @player = player
    @rune = rune
    @position = object_position
    @executions = 0
  end

  def executeAction
    runecrafting_level = @player.skill_set.get_skill(Skill::RUNECRAFT).current_level

    if runecrafting_level < @rune.level
      @player.send_message("You need a runecrafting level of #{@rune.level} to craft this rune.")
      stop
    elsif !@player.inventory.contains(RUNE_ESSENCE_ID)
      @player.send_message('You need rune essence to craft runes.')
      stop
    elsif @executions == 0
      @player.turn_to(@position)
      @player.play_animation(RUNECRAFTING_ANIMATION)
      @player.play_graphic(RUNECRAFTING_GRAPHIC)
      @executions += 1
    elsif @executions == 1
      inventory = @player.inventory
      removed = inventory.remove(RUNE_ESSENCE_ID, inventory.get_amount(RUNE_ESSENCE_ID))

      added = removed * @rune.multiplier(runecrafting_level)
      inventory.add(@rune.id, added)

      name = added > 1 ? 'some ' + @rune.name + 's' : 'an ' + @rune.name
      @player.send_message("You craft the rune essence into #{name}.")

      @player.skill_set.add_experience(Skill::RUNECRAFT, removed * @rune.experience)
      stop
    end
  end

  def equals(other)
    get_class == other.get_class && @player == other.player && @rune == other.rune
  end

end
