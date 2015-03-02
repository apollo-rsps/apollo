
private

# The array of stages in tutorial island.
STAGES = []

# The stages that are used when interacting with the Runescape Guide.
STAGES << RUNESCAPE_GUIDE = [ :not_started, :talk_to_people, :go_through_door, :runescape_guide_finished, :moving_around ]

# The stages that are used when interacting with the Survival Expert.
STAGES << SURVIVAL_EXPERT = [ :given_axe, :cut_tree, :cutting_tree, ]

quest :tutorial_island, STAGES