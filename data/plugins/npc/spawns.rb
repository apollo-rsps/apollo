require 'java'
java_import 'org.apollo.game.model.Npc'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.Position'

def register(npc)
	raise "Npc cannot be nil" if npc == nil
	World.world.register(npc)
end

### Lumbridge spawns:

# Generic: 
register Npc.new(4,    Position.new(3232, 3207))    # woman, southernmost house
register Npc.new(1,    Position.new(3231, 3237))    # man, house by willow tree
register Npc.new(2,    Position.new(3224, 3240))    # man, house by willow tree
register Npc.new(5,    Position.new(3229, 3239))    # woman, house by willow tree

# Other:
register Npc.new(0,    Position.new(3221, 3221))    # hans
register Npc.new(456,  Position.new(3243, 3210))    # father aereck
register Npc.new(519,  Position.new(3231, 3203))    # bob from bob's axes
register Npc.new(520,  Position.new(3212, 3247))    # shopkeeper
register Npc.new(521,  Position.new(3211, 3245))    # shop assistant
register Npc.new(2244, Position.new(3232, 3229))    # lumbridge guide