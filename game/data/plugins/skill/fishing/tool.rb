require 'java'

java_import 'org.apollo.game.model.Animation'

# The hash of fishing tool names to Tools.
FISHING_TOOLS = {}

# A fishing tool.
class Tool
  attr_reader :animation, :bait, :id, :message, :name

  # Creates the tool.
  def initialize(id, animation, message, bait)
    @id = id
    @bait = bait
    @animation = Animation.new(animation)
    @message = message

    @name = name_of(:item, id)
  end

end

private

# Appends a tool with the specified name to the hash.
def tool(name, hash)
  unless hash.has_keys?(:id, :animation, :message)
    fail 'Hash must contain an id, animation, and message.'
  end

  bait = hash[:bait] || []
  FISHING_TOOLS[name] = Tool.new(hash[:id], hash[:animation], hash[:message], bait)
end

# The harpoon fishing animation id.
HARPOON_ANIMATION = 618

# The cage fishing animation id.
CAGE_ANIMATION = 619

# The net fishing animation id.
NET_ANIMATION = 620

# The rod fishing animation id.
ROD_ANIMATION = 622

# TODO: The other feathers that can be used
FISHING_ROD_BAIT = [313] 
FLY_FISHING_ROD_BAIT = [314] 

tool :lobster_cage, id: 301, animation: CAGE_ANIMATION, message: 'You attempt to catch a lobster...'
tool :small_net, id: 303, animation: NET_ANIMATION, message: 'You cast out your net...'
tool :big_net, id: 305, animation: NET_ANIMATION, message: 'You cast out your net...'
tool :harpoon, id: 311, animation: HARPOON_ANIMATION, message: 'You start harpooning fish...'

tool :fishing_rod, id: 307, animation: ROD_ANIMATION, message: 'You attempt to catch a fish...',
     bait: FISHING_ROD_BAIT
tool :fly_fishing_rod, id: 309, animation: ROD_ANIMATION, message: 'You attempt to catch a fish...',
     bait: FLY_FISHING_ROD_BAIT
