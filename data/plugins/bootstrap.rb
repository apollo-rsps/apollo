# A script to 'bootstrap' all of the other plugins, wrapping Apollo's verbose
# Java-style API in a Ruby-style API.
#
# Written by Graham.

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
java_import 'org.apollo.game.login.LoginListener'
java_import 'org.apollo.game.login.LogoutListener'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.entity.Player'
java_import 'org.apollo.game.model.setting.PrivilegeLevel'
java_import 'org.apollo.game.scheduling.ScheduledTask'

# Alias the privilege levels.
RIGHTS_ADMIN    = PrivilegeLevel::ADMINISTRATOR
RIGHTS_MOD      = PrivilegeLevel::MODERATOR
RIGHTS_STANDARD = PrivilegeLevel::STANDARD

# Extends the (Ruby) String class with a method to convert a lower case,
# underscore delimited string to camel-case.
class String
  def camelize
    gsub(/(?:^|_)(.)/) { $1.upcase }
  end
end

# A CommandListener that executes a Proc object with two arguments: the player and the command.
class ProcCommandListener < CommandListener
  def initialize(rights, block)
    super(rights)
    @block = block
  end

  def execute(player, command)
    @block.call(player, command)
  end
end

# A LoginListener that executes a Proc object with the player argument.
class ProcLoginListener 
  java_implements LoginListener

  def initialize(block)
    super()
    @block = block
  end

  def execute(player)
    @block.call(player)
  end
end

# A LogoutListener that executes a Proc object with the player argument.
class ProcLogoutListener 
  java_implements LogoutListener

  def initialize(block)
    super()
    @block = block
  end

  def execute(player)
    @block.call(player)
  end
end

# An MessageHandler which executes a Proc object with three arguments: the chain
# context, the player and the message.
class ProcMessageHandler < MessageHandler
  def initialize(block, option)
    super() # required (with brackets!), see http://jira.codehaus.org/browse/JRUBY-679
    @block = block
    @option = option
  end

  def handle(ctx, player, message)
    if (@option == 0 || @option == message.option)
      @block.call(ctx, player, message)
    end
  end
end

# A ScheduledTask which executes a Proc object with one argument (itself).
class ProcScheduledTask < ScheduledTask
  def initialize(delay, immediate, block)
    super(delay, immediate)
    @block = block
  end

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
    raise 'Invalid combination of arguments.' unless (1..2).include?(args.length)
    delay = args[0]
    immediate = args.length == 2 ? args[1] : false
    $world.schedule(ProcScheduledTask.new(delay, immediate, block))
  elsif args.length == 1
    $world.schedule(args[0])
  else
    raise 'Invalid combination of arguments.'
  end
end

# Defines some sort of action to take upon an message. The following types of
# message are currently valid:
#
#   * :command
#   * :message
#   * :button
#   * :login
#   * :logout
#
# A command takes one or two arguments (the command name and optionally the
# minimum rights level to use it). The minimum rights level defaults to
# STANDARD. The block should have two arguments: player and command.
#
# An message takes no arguments. The block should have three arguments: the chain
# context, the player and the message object.
#
# A button takes one argument (the id). The block should have one argument: the
# player who clicked the button.
def on(type, *args, &block)
  case type
    when :command then on_command(args, block)
    when :message then on_message(args, block)
    when :button  then on_button(args, block)
    when :login   then on_login(block)
    when :logout  then on_logout(block)
    else raise 'Unknown message type.'
  end
end

# Defines an action to be taken upon a button press.
def on_button(args, proc)
  raise 'Button must have one argument.' unless args.length == 1

  id = args[0].to_i

  on :message, :button do |ctx, player, message|
    proc.call(player) if message.widget_id == id
  end
end

# Defines an action to be taken upon a message.
# The message can either be a symbol with the lower-case underscored class name, or the class itself.
def on_message(args, proc)
  raise 'Message must have one or two arguments.' unless (1..2).include?(args.length)
  numbers = [ 'first', 'second', 'third', 'fourth', 'fifth' ]
  message = args[0]; option = 0

  numbers.each_index do |index|
    number = numbers[index]

    if message.to_s.start_with?(number)
      option = index + 1
      message = message[number.length + 1, message.length].to_sym
      break
    end
  end

  if message.is_a?(Symbol)
    class_name = message.to_s.camelize.concat('Message')
    message = Java::JavaClass.for_name("org.apollo.game.message.impl.#{class_name}")
  end

  $ctx.add_last_message_handler(message, ProcMessageHandler.new(proc, option))
end

# Defines an action to be taken upon a command.
def on_command(args, proc)
  raise 'Command message must have one or two arguments.' unless (1..2).include?(args.length)

  rights = args.length == 2 ? args[1] : RIGHTS_STANDARD
  $ctx.add_command_listener(args[0].to_s, ProcCommandListener.new(rights, proc))
end

# Defines an action to be taken upon login.
def on_login(proc)
  $ctx.add_login_listener(ProcLoginListener.new(proc))
end

# Defines an action to be taken upon logout.
def on_logout(proc)
  $ctx.add_logout_listener(ProcLogoutListener.new(proc))
end

# Ids of in-game skills.
ATTACK_SKILL_ID = 0
DEFENCE_SKILL_ID = 1
STRENGTH_SKILL_ID = 2
HITPOINTS_SKILL_ID = 3
RANGED_SKILL_ID = 4
PRAYER_SKILL_ID = 5
MAGIC_SKILL_ID = 6
COOKING_SKILL_ID = 7
WOODCUTTING_SKILL_ID = 8
FLETCHING_SKILL_ID = 9
FISHING_SKILL_ID = 10
FIREMAKING_SKILL_ID = 11
CRAFTING_SKILL_ID = 12
SMITHING_SKILL_ID = 13
MINING_SKILL_ID = 14
HERBLORE_SKILL_ID = 15
AGILITY_SKILL_ID = 16
THIEVING_SKILL_ID = 17
SLAYER_SKILL_ID = 18
FARMING_SKILL_ID = 19
RUNECRAFT_SKILL_ID = 20