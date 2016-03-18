require 'java'

java_import 'org.apollo.game.model.inter.dialogue.DialogueAdapter'
java_import 'org.apollo.game.message.impl.CloseInterfaceMessage'
java_import 'org.apollo.game.message.impl.SetWidgetItemModelMessage'
java_import 'org.apollo.game.message.impl.SetWidgetNpcModelMessage'
java_import 'org.apollo.game.message.impl.SetWidgetPlayerModelMessage'
java_import 'org.apollo.game.message.impl.SetWidgetModelAnimationMessage'
java_import 'org.apollo.game.message.impl.SetWidgetTextMessage'
java_import 'org.apollo.game.action.DistancedAction'

# The map of conversation names to Conversations.
CONVERSATIONS = {}

# Declares a conversation.
def conversation(name, &block)
  conversation = Conversation.new(name)
  conversation.instance_eval(&block)

  raise "Conversation named #{name} already exists." if CONVERSATIONS.has_key?(name)
  CONVERSATIONS[name] = conversation
end

# A distanced action which opens the dialogue when getting into interaction distance of the given npc
class OpenDialogueAction < DistancedAction
  attr_reader :player, :npc, :dialogue

  def initialize(player, npc, dialogue)
    super(0, true, player, npc.position, 1)

    @player = player
    @npc = npc
    @dialogue = dialogue
  end

  def executeAction
    @player.set_interacting_mob(@npc)
    send_dialogue(@player, @dialogue)
    stop
  end

  def equals(other)
    return (@npc == other.npc && @dialogue == other.dialogue)
  end

end

# A conversation held between two entities.
class Conversation

  # Creates the Conversation.
  def initialize(name)
    @dialogues = {}
    @starters = []
    @name = name
  end

  # Defines a dialogue, with the specified name and block.
  def dialogue(name, &block)
    raise 'Dialogues must have a name and block.' if (name.nil? || block.nil?)

    dialogue = Dialogue.new(name, self)
    dialogue.instance_eval(&block)
    dialogue.wrap

    raise "Conversations #{@name} already has a dialogue named #{name}." if @dialogues.has_key?(name)
    @dialogues[name] = dialogue

    if ((@dialogues.empty? || dialogue.has_precondition?) && dialogue.type == :npc_speech)
      npc_index = dialogue.npc
      raise 'Npc cannot be null when opening a dialogue.' if npc_index.nil?
      @starters << dialogue

      on :message, :first_npc_action do |player, message|
        npc = $world.npc_repository.get(message.index)
        if npc_index == npc.id
          @starters.each do |start|
            if dialogue.precondition(player)
              player.start_action(OpenDialogueAction.new(player, npc, dialogue))
      	      message.terminate
      	      break
      	    end
      	  end
        end
      end
    end
  end

  # Gets part of a conversation (i.e. a dialogue).
  def part(name)
  	raise "Conversation #{@name} does not contain a dialogue called #{name}." unless @dialogues.has_key?(name)
  	@dialogues[name]
  end

end

# Declares an emote, with the specified name and id.
def declare_emote(name, id)
  EMOTES[name] = id
end


# Sends the dialogue from the specified Conversation with the specified name.
def get_dialogue(conversation, name)
  CONVERSATIONS[conversation].part(name)
end


# Sends the specified dialogue.
def send_dialogue(player, dialogue)
  type = dialogue.type
  action = dialogue.action
  action.call(player) unless action.nil?

  case type
    when :message_with_item then send_item_dialogue(player, dialogue)
    when :message_with_model then send_model_dialogue(player, dialogue)
    when :npc_speech then send_npc_dialogue(player, dialogue)
    when :options then send_options_dialogue(player, dialogue)
    when :player_speech then send_player_dialogue(player, dialogue)
    when :text
    	if dialogue.has_continue? then send_text_dialogue(player, dialogue) else send_statement_dialogue(player, dialogue) end
    else raise "Unrecognised dialogue type #{type}."
  end
end

private

# The hash of emote names to ids.
EMOTES = {}

# The maximum amount of lines of text that can be displayed on a dialogue.
MAXIMUM_LINE_COUNT = 4

# The maximum amount of options that can be displayed on a dialogue.
MAXIMUM_OPTION_COUNT = 5

# The maximum width of a line, in pixels, for a dialogue with media.
MAXIMUM_MEDIA_LINE_WIDTH = 350

# The maximum width of a line, in pixels, for a dialogue with no media.
MAXIMUM_LINE_WIDTH = 430

# The possible types of a dialogue.
DIALOGUE_TYPES = [ :message_with_item, :message_with_model, :npc_speech, :options, :player_speech, :text ]

# A type of dialogue.
class Dialogue
  attr_reader :emote, :name, :media, :options, :text, :title, :type

  # Initializes the Dialogue.
  def initialize(name, conversation)
    @name = name.to_s
    @conversation = conversation
    @text = []
    @options = []
  end

  # An action that is executed when the dialogue is displayed.
  def action(&block)
  	@action = block unless block.nil?
  	@action
  end

  # Closes the dialogue interface when the player clicks the 'Click here to continue...' text.
  def close(&block)
    continue(:close => true, &block)
  end

  # Defines the event that occurs when a player clicks the 'Click here to continue...' text.
  def continue(type=nil, &block)
    raise 'Cannot add a continue event on a dialogue with options.' if (@type == :options)
    raise 'Must declare either a type or a block for a continue event.' if (type.nil? && block.nil?)

    action = decode_next_dialogue(type) unless type.nil?
    @options[0] = ->(player) { action.call(player) unless type.nil?; block.call(player) unless block.nil? }
  end

  # Sets the emote performed by the dialogue head.
  def emote(emote=nil)
  	unless emote.nil?
      raise 'Can only perform an emote on :player_speech or :npc_speech dialogues.' unless [ :npc_speech, :player_speech ].include?(@type)
      @emote = emote.kind_of?(Symbol) ? EMOTES[emote] : emote
    end

    @emote
  end

  # Returns whether or not this Dialogue has a continue option.
  def has_continue?
  	!@options.empty?
  end

  # Returns whether or not this dialogue has a precondition.
  def has_precondition?
  	!@precondition.nil?
  end

  # Gets the media of this dialogue.
  def media()
    case @type
      when :message_with_item then @item
      when :npc_speech then @npc
      when :message_with_model then @model
      else raise "Cannot get media for #{@type}."
    end
  end

  # Sets the id of the item displayed.
  def item(item=nil, scale=100)
    unless item.nil?
      raise 'Can only display an item on :message_with_item dialogues.' unless @type == :message_with_item
      @item = lookup_item(item)
      @item_scale = scale
    end

    @item
  end

  # Gets the scale of the item.
  def item_scale
    @item_scale
  end

  # Sets the id of the model displayed.
  def model(model=nil)
    unless model.nil?
      raise 'Can only display a model on :message_with_model dialogues.' unless @type == :message_with_model
      @model = model
    end

    @model
  end

  # Sets the id of the npc displayed.
  def npc(npc=nil)
  	unless npc.nil?
      raise 'Can only display an npc on :npc_speech dialogues.' unless @type == :npc_speech
      @npc = lookup_npc(npc)
    end
    @npc
  end

  # Defines an option, displaying the specified message.
  def option(message, type)
    raise 'Can only display options on an :options dialogue.' unless @type == :options
    raise "Cannot display more than #{MAXIMUM_OPTION_COUNT} options on a dialogue." unless @options.size < MAXIMUM_OPTION_COUNT

    @options[text.size] = decode_next_dialogue(type)
    @text << message
  end

  # Gets the array of options.
  def options
      @options.dup
  end

  # Sets the precondition of this dialogue.
  def precondition(player=nil, &block)
      @precondition = block unless block.nil?
      @precondition.call(player) unless player.nil?
  end

  # Appends a message to the text list.
  def text(*message)
    @text.concat(message) unless message.nil?
    @text
  end

  # Sets the title of the dialogue.
  def title(title=nil)
    @title = title unless title.nil?
    @title
  end

  # Sets the type of dialogue.
  def type(type=nil)
    unless type.nil?
      verify_dialogue_type(type)
      @type = type
    end

    @type
  end

  # Wraps text in this Dialogue, inserting extra Dialogues in the chain if necessary.
  def wrap # TODO redo this
    next if @type == :options
    lines = []
    maximum_lines = MAXIMUM_LINE_COUNT

    text = @text.first
    segments = segment_text(text)

    if (segments.size <= maximum_lines)
      lines = segments.clone
      @text = @text[1..-1]
      insert_copy(@text) if @text.size > 0
    else
      lines = segments.first(maximum_lines).clone
      segments = [ segments.drop(maximum_lines).join() ]
      insert_copy(segments << @text[1..-1].join())
    end

    @text = lines
  end

  protected

  # Copies the value of every variable from the specified Dialogue, optionally updating the text array.
  def copy_from(dialogue, text=nil)
    @emote = dialogue.emote
    @item = dialogue.item
    @model = dialogue.model
    @npc = dialogue.npc
    @options = dialogue.options
    @text = if text.nil? then dialogue.text.dup else text.dup end
    @type = dialogue.type
  end

  private

  def segment_text(text)
    maximum_width = (@type == :text) ? MAXIMUM_LINE_WIDTH : MAXIMUM_MEDIA_LINE_WIDTH

    segments = []
    index = 0; width = 0; space = 0

    while index < text.length
      char = text[index]
      space = index if char == ' '
      width += get_width(char)
      index += 1

      if (width >= maximum_width)
        segments << text[0..space]
        text = text[(space + 1)..-1]
        width = index = space = 0
      end
    end
    segments << text if ! text.empty?

    segments
  end

  # Inserts a copy of this Dialogue into the chain, but with different text.
  def insert_copy(text)
    name = @name
    index = name.index('-auto-inserted-')

    id = if index.nil? then 0 else name[name.rindex('-')..-1].to_i + 1 end
    index ||= -1
    name = "#{name[0..index]}-auto-inserted-#{id}"

    dialogue = Dialogue.new(name, @conversation)
    dialogue.copy_from(self, text.dup)
    dialogue.wrap()

    @options[0] = ->(player) { send_dialogue(player, dialogue) }
  end

  # Decodes the next dialogue interface from the hash, returning a proc.
  def decode_next_dialogue(hash) 
    hash.each_pair do |key, value|
      case key
        when :disabled then return ->(player) { }
        when :close then return ->(player) { player.send(CloseInterfaceMessage.new) }
        when :dialogue then return ->(player) { send_dialogue(player, @conversation.part(value)) }
        else raise "Unrecognised dialogue continue type #{key}."
      end
    end
  end

end

# The dialogue interface ids for dialogues that only display text, but with no 'Click here to continue...' message.
STATEMENT_DIALOGUE_IDS = [ 12788, 12790, 12793, 12797, 6179 ] # TODO

# The dialogue interface ids for dialogues that display an item and text, ordered by line count.
ITEM_DIALOGUE_IDS = [ 306, 310, 315, 321 ]

# The dialogue interface ids for dialogues that only display text, ordered by line count.
TEXT_DIALOGUE_IDS = [ 356, 359, 363, 368, 374 ]

# The dialogue interface ids for dialogues that display the head of the player, ordered by line count.
PLAYER_DIALOGUE_IDS = [ 968, 973, 979, 986 ]

# The dialogue interface ids for dialogues that display the head of an npc, ordered by line count.
NPC_DIALOGUE_IDS = [ 4882, 4887, 4893, 4900 ]

# The dialogue interface ids for option dialogues, ordered by (option_count - 1)
OPTIONS_DIALOGUE_IDS = [ 2459, 2469, 2480, 2492 ]



## TODO separate this into different Dialogue types ##

# Sends a dialogue displaying an item model and text.
def send_item_dialogue(player, dialogue)
  text = dialogue.text
  dialogue_id =  ITEM_DIALOGUE_IDS[text.size - 1]
  player.send(SetWidgetItemModelMessage.new(dialogue_id + 1, dialogue.item, dialogue.item_scale))

  indices = [ dialogue_id + 1 + 2, dialogue_id + 1 + 1, dialogue_id + 1 + 4, dialogue_id + 1 + 5 ]

  text.each_with_index { |line, index| set_text(player, indices[index], line) }
  player.interface_set.open_dialogue(ContinueDialogueAdapter.new(player, dialogue.options[0]), dialogue_id)
end

# Sends a dialogue displaying only text, with no 'Click here to continue...' button.
def send_statement_dialogue(player, dialogue)
  text = dialogue.text
  dialogue_id = STATEMENT_DIALOGUE_IDS[text.size]

  set_text(player, dialogue_id + 1, dialogue.title)
  text.each_with_index { |line, index| set_text(player, dialogue_id + 2 + index, line) }
  player.interface_set.open_dialogue_overlay(dialogue_id)
end

# Sends a dialogue displaying only text.
def send_text_dialogue(player, dialogue)
  text = dialogue.text
  dialogue_id = TEXT_DIALOGUE_IDS[text.size - 1]

  text.each_with_index { |line, index| set_text(player, dialogue_id + 1 + index, line) }
  player.interface_set.open_dialogue(ContinueDialogueAdapter.new(player, dialogue.options[0]), dialogue_id)
end

# Sends a dialogue displaying the player's head.
def send_player_dialogue(player, dialogue)
  emote = dialogue.emote

  send_generic_dialogue player, dialogue, player.username, PLAYER_DIALOGUE_IDS do |id|
    player.send(SetWidgetPlayerModelMessage.new(id + 1))
    player.send(SetWidgetModelAnimationMessage.new(id + 1, emote)) unless emote.nil?
  end
end

# Sends a dialogue displaying the head of an npc.
def send_npc_dialogue(player, dialogue)
  npc = dialogue.npc
  emote = dialogue.emote
  name = NpcDefinition.lookup(npc).name.to_s
  name = "" if (name.nil? || name == "null")

  send_generic_dialogue player, dialogue, name, NPC_DIALOGUE_IDS do |id|
    player.send(SetWidgetNpcModelMessage.new(id + 1, npc))
    player.send(SetWidgetModelAnimationMessage.new(id + 1, emote)) unless emote.nil?
  end
end

# Sends a dialogue displaying an event.
def send_generic_dialogue(player, dialogue, title, ids, &event)
  text = dialogue.text
  dialogue_id = ids[text.size - 1]
  event.call(dialogue_id) if block_given?

  set_text(player, dialogue_title_id(dialogue_id), title)

  text.each_with_index { |line, index| set_text(player, dialogue_text_id(dialogue_id, index), line) }
  player.interface_set.open_dialogue(ContinueDialogueAdapter.new(player, dialogue.options[0]), dialogue_id)
end


# Sends an options dialogue interface.
def send_options_dialogue(player, dialogue)
  options = dialogue.options
  size = options.size
  raise 'Illegal options count: must be between 2 and 5, inclusive.' unless (2..5).include?(size)

  text = dialogue.text
  dialogue_id = OPTIONS_DIALOGUE_IDS[size - 1]

  question = dialogue.title
  set_text(player, dialogue_question_id(dialogue_id), question)

  text.each_with_index { |line, index| set_text(player, dialogue_option_id(dialogue_id, index), line) }
  player.interface_set.open_dialogue(OptionDialogueAdapter.new(player, options), dialogue_id)
end


# A DialogueAdapter for dialogues with a 'Click here to continue...' message.
class ContinueDialogueAdapter < DialogueAdapter

  # Creates the ContinueDialogueAdadpter.
  def initialize(player, continue)
    super()
    @player = player
    @continue = continue
  end

  # Executes the 'continue' lambda when the player clicks the 'Click here to continue...' message.
  def continued()
    @continue.call(@player)
  end

end


# A DialogueAdapter for dialogues with a set of options that can be selected.
class OptionDialogueAdapter < DialogueAdapter

  # Creates the OptionDialogueAdadpter.
  def initialize(player, options)
    super()
    @player = player
    @options = options.dup
  end

  # Executes an option.
  def button_clicked(button)
    option = OPTIONS_DIALOGUE_IDS.find_index(button)
    options[option].call(@player)
  end

end


# Gets the widget id of the question, for an options dialogue interface.
def dialogue_question_id(id)
  id + 1
end

# Gets the widget id of a dialogue option.
def dialogue_option_id(id, option)
  id + 1 + option
end

# Gets the widget id of a dialogue text line.
def dialogue_text_id(id, line)
  id + 3 + line
end

# Gets the widget id of a dialogue title.
def dialogue_title_id(id)
  id + 2
end

# Sets the text of a widget.
def set_text(player, id, message)
  player.send(SetWidgetTextMessage.new(id, message))
end

# Verifies that the dialogue type exists.
def verify_dialogue_type(type)
  raise "Unrecognised dialogue type #{type}, expected one of #{DIALOGUE_TYPES}." unless DIALOGUE_TYPES.include?(type)
end

# The spacing of each character glyph, for the font used for dialogue. TODO decode the font from the cache.
GLYPH_SPACING = [ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                  3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 7, 14, 9, 12, 12, 4, 5,
                  5, 10, 8, 4, 8, 4, 7, 9, 7, 9, 8, 8, 8, 9, 7, 9, 9, 4, 5, 7,
                  9, 7, 9, 14, 9, 8, 8, 8, 7, 7, 9, 8, 6, 8, 8, 7, 10, 9, 9, 8,
                  9, 8, 8, 6, 9, 8, 10, 8, 8, 8, 6, 7, 6, 9, 10, 5, 8, 8, 7, 8,
                  8, 7, 8, 8, 4, 7, 7, 4, 10, 8, 8, 8, 8, 6, 8, 6, 8, 8, 9, 8,
                  8, 8, 6, 4, 6, 12, 3, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                  3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                  4, 8, 11, 8, 8, 4, 8, 7, 12, 6, 7, 9, 5, 12, 5, 6, 10, 6, 6, 6,
                  8, 8, 4, 5, 5, 6, 7, 11, 11, 11, 9, 9, 9, 9, 9, 9, 9, 13, 8, 8,
                  8, 8, 8, 4, 4, 5, 4, 8, 9, 9, 9, 9, 9, 9, 8, 10, 9, 9, 9, 9,
                  8, 8, 8, 8, 8, 8, 8, 8, 8, 13, 6, 8, 8, 8, 8, 4, 4, 5, 4, 8,
                  8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 ]

# Gets the width of a single character.
def get_width(char)
  return GLYPH_SPACING[char.ord]
end
