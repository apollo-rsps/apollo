
# Defines a quest with the specified name.
def quest(name, *stage_names)
  stages = Array.new(stage_names.size)
  stage_names.each_with_index { |stage, index| stages << stage.kind_of?(QuestStage) ? stage : QuestStage.new(stage, index, name) }

  QUESTS[name] = Quest.new(name, stages)
end

private

# The repository of quests.
QUESTS = {}

# An ingame Quest.
class Quest
  attr_reader :name

  # Creates the Quest.
  def initialize(name, stages)
    @name = name
    @stages = stages
  end

  # Gets the finishing quest stage (i.e. the stage that indicates the Player has completed the quest).
  def final_stage()
    @stages.last
  end

  # Gets the starting quest stage.
  def initial_stage()
    @stages.first
  end

  # Gets the QuestStage with the specified name.
  def stage(name)
    @stages[name]
  end

end

# A stage in a quest, indicating the progress of a Player.
class QuestStage
  attr_reader :name, :index

  # Creates the QuestProgress.
  def initialize(name, index, quest, log_text=nil)
    @name = name
    @index = index
    @quest = quest
    @log_text = log_text
  end

  # Returns whether or no this quest stage should be logged.
  def logged
    !@log_text.nil?
  end

  # Gets the log text for this stage.
  def log_text
    raise "Cannot get the log text from an unlogged quest stage." unless logged
    @log_text
  end

  # Defines the equality operator.
  def ==(name)
    @index == index_of(name)
  end

  # Defines the not equal operator.
  def !=(name)
    @index != index_of(name)
  end

  # Defines the greater than or equal to operator.
  def >=(name)
    @index >= index_of(name)
  end

  # Defines the greater than operator.
  def >(name)
    @index > index_of(name)
  end

  # Defines the less than operator.
  def <(name)
    @index < index_of(name)
  end

  # Defines the less than or equal to operator.
  def <=(name)
    @index <= index_of(name)
  end


  private

  # Gets the index of the QuestStage with the specified name.
  def index_of(name)
    QUESTS[@quest].stage(name).index
  end

end


# Define method_missing for player
class Player

  # Override method_missing to return a QuestStage if the method name indicates quest.
  def method_missing(symbol, *args)
    unless args.nil?
      arg = args[0]
      args[0] = arg.name if arg.kind_of?(QuestStage)
    end

    result = super.method_missing(symbol, args)
    string = symbol.to_s

    if (string.ends_with('_progress'))
      name = string[0..-10] # Cut the '_progress' from the end
      quest = QUESTS[name.to_sym]
      result = quest.stage(result) unless quest.nil?
      raise "No QuestStage with the name #{result} exists - define it as part of the Quest declaration." if result.nil?
    end

    result
  end

end