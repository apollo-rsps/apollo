# A script bootstrapper for the rest of the plugins, which wraps Apollo's
# verbose Java-style API in a Ruby-style API.

# ********************************** WARNING **********************************
# * If you do not really understand what this is for, do not edit it without  *
# * creating a backup! Many plugins rely on the behaviour of this script, and *
# * will break if you mess it up.                                             *
# *                                                                           *
# * This is actually part of the core server and in an ideal world shouldn't  *
# * be changed.                                                               *
# *****************************************************************************

require 'java'

java_import 'org.apollo.game.command.CommandListener'
java_import 'org.apollo.game.message.handler.MessageHandler'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.entity.Player'
java_import 'org.apollo.game.model.event.EventListener'
java_import 'org.apollo.game.model.event.PlayerEvent'
java_import 'org.apollo.game.model.event.impl.LoginEvent'
java_import 'org.apollo.game.model.event.ProxyEvent'
java_import 'org.apollo.game.model.event.ProxyEventListener'
java_import 'org.apollo.game.model.entity.setting.PrivilegeLevel'
java_import 'org.apollo.game.scheduling.ScheduledTask'
java_import 'org.apollo.game.plugin.PluginContext'

# Alias the privilege levels.
RIGHTS_ADMIN    = PrivilegeLevel::ADMINISTRATOR
RIGHTS_MOD      = PrivilegeLevel::MODERATOR
RIGHTS_STANDARD = PrivilegeLevel::STANDARD

# Extends the (Ruby) String class with a method to convert a lower case,
# underscore delimited string to camel-case.
class String

  # Converts a ruby snake_case string to camel-case.
  def camelize
    gsub(/(?:^|_)(.)/) { $1.upcase }
  end

end

# A CommandListener that executes a Proc object with two arguments: the player and the command.
class ProcCommandListener < CommandListener

  # Creates the ProcCommandListener.
  def initialize(rights, block)
    super(rights)
    @block = block
  end

  # Executes the block listening for the command.
  def execute(player, command)
    @block.call(player, command)
  end

end

# A LogoutListener that executes a Proc object with the player argument.
class ProcEventListener
  java_implements EventListener

  # Creates the ProcEventListener.
  def initialize(block)
    super()
    @block = block
  end

  # Executes the block handling the Event.
  def handle(event)
    args = [event]
    args << event.player if event.is_a?(PlayerEvent)
    @block.call(*args)
  end

end

# A MessageHandler which executes a Proc object with two arguments: the player and the message.
class ProcMessageHandler < MessageHandler

  # Creates the ProcMessageHandler.
  def initialize(block, option)
    super($world)
    @block = block
    @option = option
  end

  # Handles the message.
  def handle(player, message)
    @block.call(player, message) if @option == 0 || @option == message.option
  end

end

# A ScheduledTask which executes a Proc object with one argument (itself).
class ProcScheduledTask < ScheduledTask

  # Creates the ProcScheduledTask.
  def initialize(delay, immediate, block)
    super(delay, immediate)
    @block = block
  end

  # Executes the block.
  def execute
    @block.call(self)
  end

end

# Schedules a ScheduledTask. Can be used in two ways: passing an existing
# ScheduledTask object or passing a block along with one or two parameters: the
# delay (in pulses) and, optionally, the immediate flag.
#
# If the immediate flag is not given, it defaults to false.
#
# The ScheduledTask object is passed to the block so that methods such as
# setDelay and stop can be called. execute MUST NOT be called - if it is, the
# behaviour is undefined (and most likely it'll be bad).
def schedule(*args, &block)
  if block_given?
    fail 'Invalid combination of arguments.' unless (1..2).include?(args.length)
    delay = args[0]

    immediate = args.length == 2 ? args[1] : false
    $world.schedule(ProcScheduledTask.new(delay, immediate, block))
  elsif args.length == 1
    $world.schedule(args[0])
  else
    fail 'Invalid combination of arguments.'
  end
end

@@proxy_listener = ProxyEventListener.new
$world.listen_for(ProxyEvent.java_class, @@proxy_listener)

# Defines some sort of action to take upon an message. The following types of message are currently
# valid:
#
#   * :command
#   * :message
#   * :button
#   * Any valid Event, as a symbol in ruby snake_case form.
#
# A command takes one or two arguments (the command name and optionally the minimum rights level to
# use it). The minimum rights level defaults to STANDARD. The block should have two arguments:
# player and command.
#
# An message takes no arguments. The block should have two arguments: the player and the message
# object.
#
# A button takes one argument (the id). The block should have one argument: the player who clicked
# the button.
def on(type, *args, &block)
  case type
    when :command then on_command(args, block)
    when :message then on_message(args, block)
    when :button  then on_button(args, block)
    else
      class_name = type.to_s.camelize.concat('Event')

      begin
        type = Java::JavaClass.for_name("org.apollo.game.model.event.impl.#{class_name}")
      rescue
        @@proxy_listener.add(class_name, ProcEventListener.new(block))
        return
      end

      $world.listen_for(type, ProcEventListener.new(block))
  end
end

# Contains extension methods for World.
module WorldExtensions

  # Overrides World#submit, providing special-case behaviour for Events defined in Ruby, which
  # need to be wrapped in a ProxyEvent, until https://github.com/jruby/jruby/issues/2359 is
  # resolved.
  def submit(event)
    if event.java_class.name.end_with?(".Event", ".PlayerEvent")
      event = ProxyEvent.new(event.class.name, event)
    end

    super(event)
  end

end

# Prepend the methods defined in WorldExtensions to World.
class World
  prepend WorldExtensions
end

private

# Defines an action to be taken upon a button press.
def on_button(args, proc)
  fail 'Button must have one argument.' unless args.length == 1

  id = args[0].to_i

  on :message, :button do |player, message|
    proc.call(player) if message.widget_id == id
  end
end

# Defines an action to be taken upon a message.
# The message can either be a symbol with the lowercase underscored class name, or the class itself.
def on_message(args, proc)
  fail 'Message must have one or two arguments.' unless (1..2).include?(args.length)

  numbers = %w(first second third fourth fifth)
  message = args[0].to_s
  option = 0

  (0...numbers.length).each do |index|
    number = numbers[index]

    if message.start_with?(number)
      option = index + 1
      message = message[number.length + 1, message.length]
      break
    end
  end

  class_name = message.camelize.concat('Message')
  message = Java::JavaClass.for_name("org.apollo.game.message.impl.#{class_name}")

  $ctx.add_message_handler(message, ProcMessageHandler.new(proc, option))
end

# Defines an action to be taken upon a command.
def on_command(args, proc)
  fail 'Command message must have one or two arguments.' unless (1..2).include?(args.length)

  rights = args.length == 2 ? args[1] : RIGHTS_STANDARD
  $world.command_dispatcher.register(args[0].to_s, ProcCommandListener.new(rights, proc))
end
