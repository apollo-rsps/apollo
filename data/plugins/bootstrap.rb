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
java_import 'org.apollo.game.event.handler.EventHandler'
java_import 'org.apollo.game.login.LoginListener'
java_import 'org.apollo.game.login.LogoutListener'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.entity.Player'
java_import 'org.apollo.game.model.settings.PrivilegeLevel'
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

# A CommandListener that executes a Proc object with two arguments: the player
# and the command.
class ProcCommandListener < CommandListener
  def initialize(rights, block)
    super rights
    @block = block
  end

  def execute(player, command)
    @block.call(player, command)
  end
end

# A LoginListener that executes a Proc object with the player argument.
class ProcLoginListener < LoginListener
  def initialize(block)
    super()
    @block = block
  end

  def execute(player)
    @block.call(player)
  end
end

# A LogoutListener that executes a Proc object with the player argument.
class ProcLogoutListener < LogoutListener
  def initialize(block)
    super()
    @block = block
  end

  def execute(player)
    @block.call(player)
  end
end

# An EventHandler which executes a Proc object with three arguments: the chain
# context, the player and the event.
class ProcEventHandler < EventHandler
  def initialize(block)
    super() # required (with brackets!), see http://jira.codehaus.org/browse/JRUBY-679
    @block = block
  end

  def handle(ctx, player, event)
    @block.call(ctx, player, event)
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
    raise 'Invalid combination of arguments.' unless args.length == 1 or args.length == 2
    delay = args[0]
    immediate = args.length == 2 ? args[1] : false
    World.world.schedule(ProcScheduledTask.new(delay, immediate, block))
  elsif args.length == 1
    World.world.schedule(args[0])
  else
    raise 'Invalid combination of arguments.'
  end
end

# Defines some sort of action to take upon an event. The following types of
# event are currently valid:
#
#   * :command
#   * :event
#   * :button
#   * :login
#   * :logout
#
# A command takes one or two arguments (the command name and optionally the
# minimum rights level to use it). The minimum rights level defaults to
# STANDARD. The block should have two arguments: player and command.
#
# An event takes no arguments. The block should have three arguments: the chain
# context, the player and the event object.
#
# A button takes one argument (the id). The block should have one argument: the
# player who clicked the button.
def on(type, *args, &block)
  case type
    when :command then on_command(args, block)
    when :event   then on_event(args, block)
    when :button  then on_button(args, block)
    when :login   then on_login(block)
    when :logout  then on_logout(block)
    else raise 'Unknown event type.'
  end
end

# Defines an action to be taken upon a button press.
def on_button(args, proc)
  raise 'Button must have one argument.' unless args.length == 1

  id = args[0].to_i

  on :event, :button do |ctx, player, event|
    proc.call(player) if event.widget_id == id
  end
end

# Defines an action to be taken upon an event.
# The event can either be a symbol with the lower case, underscored class name
# or the class itself.
def on_event(args, proc)
  raise "event must have one argument" unless args.length == 1

  event = args[0]
  if event.is_a?(Symbol)
    class_name = event.to_s.camelize.concat('Event')
    event = Java::JavaClass.for_name("org.apollo.game.event.impl.#{class_name}")
  end

  $ctx.add_last_event_handler(event, ProcEventHandler.new(proc))
end

# Defines an action to be taken upon a command.
def on_command(args, proc)
  raise 'Command event must have one or two arguments.' unless (1..2).include?(args.length)

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