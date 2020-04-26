require 'java'

java_import 'org.apollo.game.action.Action'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.entity.Skill'

BURY_BONE_ANIMATION = Animation.new(827)
BONES = {}

# A bone with an id and experience value.
class Bone
  attr_reader :id, :experience

  def initialize(id, experience)
    @id = id
    @experience = experience
  end

end

# An action where a bone in a player's inventory is buried.
class BuryBoneAction < Action
  attr_reader :slot, :bone

  def initialize(mob, slot, bone)
    super(1, true, mob)
    @slot = slot
    @bone = bone
    @executions = 0
  end

  def execute
    if @executions == 0
      mob.send_message('You dig a hole in the ground...')
      mob.play_animation(BURY_BONE_ANIMATION)
      @executions += 1
    elsif @executions == 1
      if mob.inventory.get(@slot).id == @bone.id
        mob.send_message('You bury the bones.')
        mob.inventory.reset(@slot)
        mob.skill_set.add_experience(Skill::PRAYER, @bone.experience)
      end
      stop
    end
  end

  def equals(other)
    get_class == other.get_class
  end

end

# Intercepts the first item option message.
on :message, :first_item_option do |player, message|
  bone = BONES[message.id]

  unless bone.nil?
    player.start_action(BuryBoneAction.new(player, message.slot, bone))
    message.terminate
  end
end

# Appends a bone to the array
def append_bone(hash)
  fail 'Hash must contain an id and an experience value.' unless hash.has_keys?(:id, :experience)
  id = hash[:id]
  BONES[id] = Bone.new(id, hash[:experience])
end

append_bone name: :regular_bones,     id: 526,  experience: 5
append_bone name: :burnt_bones,       id: 528,  experience: 5
append_bone name: :bat_bones,         id: 530,  experience: 4
append_bone name: :big_bones,         id: 532,  experience: 45
append_bone name: :babydragon_bones,  id: 534,  experience: 30
append_bone name: :dragon_bones,      id: 536,  experience: 72
append_bone name: :wolf_bones,        id: 2859, experience: 14
append_bone name: :shaikahan_bones,   id: 3123, experience: 25
append_bone name: :jogre_bones,       id: 3125, experience: 15
append_bone name: :burnt_zogre_bones, id: 3127, experience: 25
append_bone name: :monkey_bones,      id: 3179, experience: 14 # smallish
append_bone name: :monkey_bones,      id: 3180, experience: 14 # medium
append_bone name: :monkey_bones,      id: 3181, experience: 14 # quite large
append_bone name: :monkey_bones,      id: 3182, experience: 14 # quite large
append_bone name: :monkey_bones,      id: 3183, experience: 14 # small
append_bone name: :shaking_bones,     id: 3187, experience: 14
append_bone name: :zogre_bones,       id: 4812, experience: 23
append_bone name: :fayrg_bones,       id: 4830, experience: 84
append_bone name: :raurg_bones,       id: 4832, experience: 96
append_bone name: :ourg_bones,        id: 4834, experience: 140
