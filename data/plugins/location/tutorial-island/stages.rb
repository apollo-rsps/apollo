
private

# The array of stages in tutorial island.
STAGES = []

# The stages that are used when interacting with the Runescape Guide.
RUNESCAPE_GUIDE = [:not_started, :talk_to_people, :go_through_door, :runescape_guide_finished,
                   :moving_around]
STAGES.concat(RUNESCAPE_GUIDE)

# The stages that are used when interacting with the Survival Expert.
SURVIVAL_EXPERT = [:given_axe, :cut_tree, :cutting_tree]
STAGES.concat(SURVIVAL_EXPERT)

quest :tutorial_island, STAGES
