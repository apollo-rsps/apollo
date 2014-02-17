require 'java'

java_import 'org.apollo.game.action.Action'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.Skill'

PRAYER_SKILL_ID = Skill::PRAYER
BURY_BONE_ANIMATION = 827
BONES = {}

# Represents a bone with a name, id, and experience.
class Bone
 attr_reader :name, :id, :exp

  def initialize(name, id, exp)
    @name = name
    @id = id
    @exp = exp
  end

end

# An action where a bone in a player's inventory is buried.
class BuryBoneAction < Action
  
  def initialize(mob, slot, bone)
    super(2, false, mob)
    @slot = slot
    @bone = bone
    mob.play_animation(Animation.new(BURY_BONE_ANIMATION))
    mob.send_message('You dig a hole in the ground...')
  end
  
  def execute
    if mob.inventory.get(@slot).id == @bone.id
      mob.send_message('You bury the bones.')
      mob.inventory.reset(@slot)
      mob.skill_set.add_experience(PRAYER_SKILL_ID, @bone.exp)
    end
    stop
  end

end

# Intercepts the item option event, 
on :event, :item_option do |ctx, player, event|
  if event.option == 1
    bone = BONES[event.id]
    unless bone == nil
      player.start_action(BuryBoneAction.new(player, event.slot, bone))
      ctx.break_handler_chain
    end
  end
end

# Appends a bone to the array
def append_bone(hash)
  raise 'Hash must contain an id and an experience value.' unless hash.has_key?(:id) && hash.has_key?(:experience)
  name = hash[:name], id = hash[:id], experience = hash[:experience]
  BONES[id] = Bone.new(name, id, experience)
end

append_bone :name => :regular_bones,     :id => 526,  :experience => 5
append_bone :name => :burnt_bones,       :id => 528,  :experience => 5
append_bone :name => :bat_bones,         :id => 530,  :experience => 4
append_bone :name => :big_bones,         :id => 532,  :experience => 45
append_bone :name => :babydragon_bones,  :id => 534,  :experience => 30
append_bone :name => :dragon_bones,      :id => 536,  :experience => 72
append_bone :name => :wolf_bones,        :id => 2859, :experience => 14
append_bone :name => :shaikahan_bones,   :id => 3123, :experience => 25
append_bone :name => :jogre_bones,       :id => 3125, :experience => 15
append_bone :name => :burnt_zogre_bones, :id => 3127, :experience => 25
append_bone :name => :monkey_bones,      :id => 3179, :experience => 14 # smallish
append_bone :name => :monkey_bones,      :id => 3180, :experience => 14 # medium
append_bone :name => :monkey_bones,      :id => 3181, :experience => 14 # quite large
append_bone :name => :monkey_bones,      :id => 3182, :experience => 14 # quite large
append_bone :name => :monkey_bones,      :id => 3183, :experience => 14 # small
append_bone :name => :shaking_bones,     :id => 3187, :experience => 14
append_bone :name => :zogre_bones,       :id => 4812, :experience => 23
append_bone :name => :fayrg_bones,       :id => 4830, :experience => 84
append_bone :name => :raurg_bones,       :id => 4832, :experience => 96
append_bone :name => :ourg_bones,        :id => 4834, :experience => 140