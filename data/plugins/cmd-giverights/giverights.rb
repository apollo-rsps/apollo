require 'java'
java_import 'org.apollo.game.model.World'

#Gives the specified player the specified rights.
on :command, :giverights do |player, command|
	args = command.arguments
  unless args.length == 2
    player.send_message('Invalid syntax - ::item [player] [rightslevel]')
    next
  end
    
  name = args[0]
  level = args[1].to_i
  if (level > 2)
    player.send_message('The rights level is to high, it goes 0(player), 1(mod), 2(admin).')
    next
  end
  
  target = World.world.get_player name
  if (target == nil)
    player.send_message('No such player online.')
    next
  end
  
  target.privilege_level = level > 0 ? (level == 2 ? RIGHTS_ADMIN : RIGHTS_MOD) : RIGHTS_STANDARD  
  
  message = "#{name} was made " + (level > 0 ? (level == 2 ? "an admin!" : "a mod!") : "a player.")
  player.send_message(message)  
end