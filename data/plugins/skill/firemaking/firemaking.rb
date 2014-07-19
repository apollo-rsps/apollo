require 'java'

java_import 'org.apollo.game.action.Action'
java_import 'org.apollo.game.model.entity.Skill'

FIRE_OBJECT_ID = 2732

LOGS = {}

LIGHTERS = {}

class Log
  attr_reader :id, :level, :experience
  
  def initialize(id, level, experience)
    @id = id
    @level = level
    @experience = experience
  end

end

# An action where a player lights a log.
class LogLightingAction < Action

  def initialize(player, log, animation)
    super(1, true, player)
    @log = log
    @time = 1
  end

  def execute
    if time == 0
      player.play_animation(animation)
      # TODO drop logs, spawn obj
    else
      time -= 1
    end
  end

end

# Appends a log to the hash.
def append_log(hash)
  raise "Hash must contain an id, level, and experience value." unless hash.has_key?(:id) && hash.has_key?(:level) && hash.has_key?(:experience)
  id = hash[:id]; level = hash[:level]; experience = hash[:experience]

  LOGS[id] = Log.new(level, experience)
end

# Appends a lighter to the hash.
def append_lighter(id, animation_id)
  LIGHTERS[id] = animation_id
end