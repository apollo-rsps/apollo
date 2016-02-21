java_import 'org.apollo.cache.def.ItemDefinition'

#-----------------Herblore Helper Methods-----------------

MIXING_ANIMATION = 363

# Example wrapper method.
def create_unfinished_potion(name, amount = 1, &block)
  recipe = create_recipe name, amount do

    message :attempt, lambda { |player, primary, secondary|
      primary_name = ItemDefinition.lookup(primary).name
      secondary_name = ItemDefinition.lookup(secondary).name

      "You use the #{primary_name} with the #{secondary_name}."
    }
    
    requires :material, name: :vial_of_water

    set_animation MIXING_ANIMATION
    set_action_type :repeatable

    self.instance_eval(&block)
  end
end

#-----------------Start of making unfinished potions-----------------

# Create Unfinished Guam Potion
create_unfinished_potion :unfinished_guam_potion_91 do
  requires :skill, id: Skill::HERBLORE, level: 1
  requires :material, name: :guam_leaf
end

# Create Unfinished Marrentill Potion
create_unfinished_potion :unfinished_potion_93 do
  requires :skill, id: Skill::HERBLORE, level: 5
  requires :material, name: :marrentill
end

# Create Unfinished Tarromin Potion
create_unfinished_potion :unfinished_potion_95 do
  requires :skill, id: Skill::HERBLORE, level: 12
  requires :material, name: :tarromin
end

# Create Unfinished Harralander Potion
create_unfinished_potion :unfinished_potion_97 do
  requires :skill, id: Skill::HERBLORE, level: 22
  requires :material, name: :harralander
end

# Create Unfinished Ranarr Potion
create_unfinished_potion :unfinished_potion_99 do
  requires :skill, id: Skill::HERBLORE, level: 30
  requires :material, name: :ranarr_weed
end

# Create Unfinished Toadflax Potion
create_unfinished_potion :unfinished_potion_3002 do
  requires :skill, id: Skill::HERBLORE, level: 34
  requires :material, name: :toadflax
end

# Create Unfinished Irit Potion
create_unfinished_potion :unfinished_potion_101 do
  requires :skill, id: Skill::HERBLORE, level: 45
  requires :material, name: :irit_leaf
end

# Create Unfinished Avantoe Potion
create_unfinished_potion :unfinished_potion_103 do
  requires :skill, id: Skill::HERBLORE, level: 50
  requires :material, name: :avantoe
end

# Create Unfinished Kwaurm Potion
create_unfinished_potion :unfinished_potion_105 do
  requires :skill, id: Skill::HERBLORE, level: 55
  requires :material, name: :kwuarm
end

# Create Unfinished Snapdragon Potion
create_unfinished_potion :unfinished_potion_3004 do
  requires :skill, id: Skill::HERBLORE, level: 63
  requires :material, name: :snapdragon
end

# Create Unfinished Cadantine Potion
create_unfinished_potion :unfinished_potion_107 do
  requires :skill, id: Skill::HERBLORE, level: 66
  requires :material, name: :cadantine
end

# Create Unfinished Lantadyme Potion
create_unfinished_potion :unfinished_potion_2483 do
  requires :skill, id: Skill::HERBLORE, level: 69
  requires :material, name: :lantadyme
end

# Create Unfinished Dwarf Weed Potion
create_unfinished_potion :unfinished_potion_109 do
  requires :skill, id: Skill::HERBLORE, level: 72
  requires :material, name: :dwarf_weed
end

# Create Unfinished Torstol Potion
create_unfinished_potion :unfinished_potion_111 do
  requires :skill, id: Skill::HERBLORE, level: 78
  requires :material, name: :torstol
end