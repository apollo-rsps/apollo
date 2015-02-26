require 'java'

java_import 'org.apollo.game.model.inter.dialogue.DialogueAdapter'
java_import 'org.apollo.game.message.impl.CloseInterfaceMessage'
java_import 'org.apollo.game.message.impl.SetWidgetItemModelMessage'
java_import 'org.apollo.game.message.impl.SetWidgetNpcModelMessage'
java_import 'org.apollo.game.message.impl.SetWidgetPlayerModelMessage'
java_import 'org.apollo.game.message.impl.SetWidgetTextMessage'

# Defines a dialogue, with the specified name and block.
def dialogue(name, &block)
  raise 'Dialogues must have a name and block.' if (name.nil? || block.nil?)

  dialogue = Dialogue.new(name)
  dialogue.instance_eval(&block)
  dialogue.wrap
  DIALOGUES[name] = dialogue
end

# Defines an opening (i.e. conversation starter) dialogue, which hooks into the chain.
# Allows for a lambda prerequisite to be passed, which takes one argument the player; if the prerequisite evaluates to false, the dialogue will not be opened.
def opening_dialogue(name, prerequisite=nil, &block)
  dialogue = dialogue(name, &block)
  npc = dialogue.npc
  raise 'Npc cannot be null when opening a dialogue.' if npc.nil?

  on :message, :first_npc_action, npc do |ctx, player, event|
    player.open_dialogue(name) if (prerequisite.nil? || prerequisite.call(player))
  end
end

# Declares an emote, with the specified name and id.
def declare_emote(name, id)
  EMOTES[name] = id
end



private

# The hash of dialogue names to dialogues.
DIALOGUES = {}

# The hash of emote names to ids.
EMOTES = {}

# The maximum amount of lines of text that can be displayed on a dialogue.
MAXIMUM_LINE_COUNT = 4

# The maximum amount of options that can be displayed on a dialogue.
MAXIMUM_OPTION_COUNT = 5

# The maximum width of a line, in characters.
MAXIMUM_LINE_WIDTH = 55

# The possible types of a dialogue.
DIALOGUE_TYPES = [ :message_with_item, :message_with_model, :npc_speech, :options, :player_speech, :text ]

# A type of dialogue.
class Dialogue
  attr_reader :emote, :name, :media, :options, :text, :title, :type

  # Initializes the Dialogue.
  def initialize(name)
    @name = name.to_s
    @text = []
    @options = []
  end

  # Closes the dialogue interface when the player clicks the 'Click here to continue...' text.
  def close
    continue(:close => true)
  end

  # Defines the event that occurs when a player clicks the 'Click here to continue...' text.
  def continue(type=nil, &block)
    raise 'Cannot add a continue event on a dialogue with options.' unless @options.size.zero?
    raise 'Must declare either a type or a block for a continue event.' if (type.nil? && block.nil?)

    @options << (block.nil? ? get_next_dialogue(type) : block)
  end

  # Sets the emote performed by the dialogue head.
  def emote(emote=nil)
    raise 'Can only perform an emote on :player_speech or :npc_speech dialogues.' unless [ :npc_speech, :player_speech ].include?(@type)
    @emote = EMOTES[emote] if emote.kind_of?(Symbol)
    @emote
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
  def item(item=nil, scale=nil)
    unless item.nil?
      raise 'Can only display an item on :message_with_item dialogues.' unless @type == :message_with_item
      @item = item
      @item_scale = scale
    end

    @item
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
    raise 'Can only display an npc on :npc_speech dialogues.' unless @type == :npc_speech
    @npc = lookup_npc(npc) unless npc.nil?
    @npc
  end

  # Defines an option, displaying the specified message.
  def option(message, type)
    raise 'Can only display options on an :options dialogue.' unless @type == :options
    raise "Cannot display more than #{MAXIMUM_OPTION_COUNT} options on a dialogue." unless @options.size < MAXIMUM_OPTION_COUNT

    @options[text.size] = get_next_dialogue(type)
    @text << message
  end

  # Gets the array of options.
  def options
  	@options.dup
  end

  # Appends a message to the text list.
  def text(*message)
    unless message.nil?
      @text.concat(message)
    end

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
  def wrap
    lines = []
    next if @type == :options

    text = @text[0]
    segments = []# text.chars.each_slice(MAXIMUM_LINE_WIDTH).map(&:join) # Split text into array of strings with length <= 60.
    previous = 0; index = MAXIMUM_LINE_WIDTH

    while index < text.length
      index -= 1 until text[index] == ' '
      segments << text[previous..index]
      previous = index
      index += MAXIMUM_LINE_WIDTH
    end
    segments << text[previous..text.length]

    if (segments.size <= MAXIMUM_LINE_COUNT)
      lines.concat(segments)
      @text = @text.drop(1)
      insert_copy(@text) if @text.size > 0
    else
      remaining = MAXIMUM_LINE_COUNT - segments.size
      lines.concat(segments.first(remaining))
      insert_copy(segments.drop(remaining).join().concat(@text.drop(1)))
    end

    @text = lines
  end


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

  # Inserts a copy of this Dialogue into the chain, but with different text.
  def insert_copy(text)
    name = @name
    index = name.index('-auto-inserted-')

    id = if index.nil? then 0 else name[name.rindex('-')..-1].to_i + 1 end
    index ||= -1
    name = "#{name[0..index]}-auto-inserted-#{id}"

    dialogue = Dialogue.new(name)
    dialogue.copy_from(self, text.dup)
    dialogue.wrap()

    DIALOGUES[name] = dialogue
    @options[0] = ->(player) { send_dialogue(player, dialogue) }
  end

  # Decodes the next dialogue interface from the hash, returning a proc.
  def get_next_dialogue(hash)
    hash.keys.each do |key|
      case key
        when :close
          return ->(player) { player.send(CloseInterfaceMessage.new) }
        when :dialogue
          return ->(player) { send_dialogue(player, lookup_dialogue(hash[key])) }
        else raise "Unrecognised dialogue continue type #{key}."
      end
    end
  end

end

# The existing Player class.
class Player

  # Opens the dialogue with the specified name.
  def open_dialogue(name)
    dialogue = lookup_dialogue(name)
    send_dialogue(self, dialogue)
  end

end



# Gets a Dialogue using the name it was registered with.
def lookup_dialogue(name)
  dialogue = DIALOGUES[name]
  raise "No dialogue named #{name.to_s}." if dialogue.nil?

  dialogue
end

# Sends the specified dialogue.
def send_dialogue(player, dialogue)
  type = dialogue.type

  case type
    when :message_with_item then send_item_dialogue(player, dialogue)
    when :message_with_model then send_model_dialogue(player, dialogue)
    when :npc_speech then send_npc_dialogue(player, dialogue)
    when :options then send_options_dialogue(player, dialogue)
    when :player_speech then send_player_dialogue(player, dialogue)
    when :text then send_text_dialogue(player, dialogue)
    else raise "Unrecognised dialogue type #{type}."
  end
end

# The dialogue interface ids for dialogues that only display text, ordered by line count.
TEXT_DIALOGUE_IDS = [ 356, 359, 363, 368, 374 ]

# The dialogue interface ids for dialogues that display the head of the player, ordered by line count.
PLAYER_DIALOGUE_IDS = [ 968, 973, 979, 986 ]

# The dialogue interface ids for dialogues that display the head of an npc, ordered by line count.
NPC_DIALOGUE_IDS = [ 4882, 4887, 4893, 4900 ]

# The dialogue interface ids for option dialogues, ordered by (option_count - 1)
OPTIONS_DIALOGUE_IDS = [ 2459, 2469, 2480, 2492 ]

# Sends a dialogue displaying only text.
def send_text_dialogue(player, dialogue)
  title = dialogue.title
  send_generic_dialogue(player, dialogue, title, TEXT_DIALOGUE_IDS)
end

# Sends a dialogue displaying the player's head.
def send_player_dialogue(player, dialogue)
  send_generic_dialogue(player, dialogue, PLAYERS_DIALOGUE_IDS, ->(id) { SetWidgetPlayerModelMessage.new(id + 1) })
end

# Sends a dialogue displaying the head of an npc.
def send_npc_dialogue(player, dialogue)
  npc = dialogue.npc
  name = NpcDefinition.lookup(npc).name.to_s
  name = "" if (name.nil? || name == "null")

  send_generic_dialogue(player, dialogue, name, NPC_DIALOGUE_IDS, ->(id) { SetWidgetNpcModelMessage.new(id + 1, npc)})
end


# Sends a dialogue displaying an event.
def send_generic_dialogue(player, dialogue, title, ids, event=nil)
  text = dialogue.text
  dialogue_id = ids[text.size - 1]
  player.send(event.call(dialogue_id)) unless event.nil?

  set_text(player, dialogue_title_id(dialogue_id), title)

  text.each_index { |index| set_text(player, dialogue_text_id(dialogue_id, index), text[index]) }
  player.interface_set.open_dialogue(ContinueDialogueAdapter.new(player, dialogue.options[0]), dialogue_id) # TODO listener!!!
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

  text.each_index { |index| set_text(player, dialogue_option_id(dialogue_id, index), text[index]) }
  player.interface_set.open_dialogue(OptionDialogueAdapter.new(player, options), dialogue_id) # TODO listener!!!
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

def get_width(char)
  
end