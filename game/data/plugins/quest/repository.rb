
# Defines a quest with the specified name.
def quest(name, stage_names)
  stages = { }
  stage_names.each_with_index { |stage, index| stages[stage] = QuestStage.new(stage, index, name) }

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
    raise "Quest name must be a symbol, received '#{name}'." unless name.kind_of?(Symbol)
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
    stage = @stages[name]
    raise "No stage named #{name} exists in #{@name}." if stage.nil?
    stage
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

    result = super(symbol, *args)
    string = symbol.to_s

    if (string.end_with?('_progress'))
      name = string[0..-10] # Cut the '_progress' from the end
      quest = QUESTS[name.to_sym]
      raise "No Quest with the name '#{name}' exists." if quest.nil?

      result = quest.stage(result)
    end

    result
  end

end