require 'java'

java_import 'org.apollo.game.model.Animation'

# The hash of fishing tool names to Tools.
FISHING_TOOLS = {}

# A fishing tool.
class Tool
  attr_reader :id, :bait, :animation, :message, :name

  # Creates the tool.
  def initialize(id, bait=[], animation, message)
    @id = id
    @bait = bait
    @animation = Animation.new(animation)
    @message = message

    @name = name_of(:item, id)
  end

end


private

# Appends a tool with the specified name to the hash.
def append_tool(name, tool)
  FISHING_TOOLS[name] = tool
end

HARPOON_ANIMATION_ID = 618
CAGE_ANIMATION_ID = 619
NET_ANIMATION_ID = 620
ROD_ANIMATION_ID = 622

# TODO The other feathers that can be used
FISHING_ROD_BAIT = [ 313 ] 
FLY_FISHING_ROD_BAIT = [ 314 ] 

append_tool(:lobster_cage, Tool.new(301, CAGE_ANIMATION_ID,    'You attempt to catch a lobster...'))
append_tool(:small_net,    Tool.new(303, NET_ANIMATION_ID,     'You cast out your net...'))
append_tool(:big_net,      Tool.new(305, NET_ANIMATION_ID,      'You cast out your net...'))
append_tool(:harpoon,      Tool.new(311, HARPOON_ANIMATION_ID, 'You start harpooning fish...'))

append_tool(:fishing_rod,     Tool.new(307, FISHING_ROD_BAIT,     ROD_ANIMATION_ID, 'You attempt to catch a fish...'))
append_tool(:fly_fishing_rod, Tool.new(309, FLY_FISHING_ROD_BAIT, ROD_ANIMATION_ID, 'You attempt to catch a fish...'))
