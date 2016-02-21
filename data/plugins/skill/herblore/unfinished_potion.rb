java_import 'org.apollo.cache.def.ItemDefinition'

#-----------------Herblore Helper Methods-----------------

MIXING_ANIMATION = 363

# Example wrapper method.
def create_unfinished_potion(name, &block)
  recipe = create_recipe name do

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
create_unfinished_potion :unfinished_guam_potion do
  requires :skill, id: Skill::HERBLORE, level: 1
  requires :material, name: :guam_leaf

  rewards :product, name: :unfinished_potion_91
end

# Create Unfinished Marrentill Potion
create_unfinished_potion :unfinished_marrentill_potion do
  requires :skill, id: Skill::HERBLORE, level: 5
  requires :material, name: :marrentill

  rewards :product, name: :unfinished_potion_93
end

# Create Unfinished Tarromin Potion
create_unfinished_potion :unfinished_tarromin_potion do
  requires :skill, id: Skill::HERBLORE, level: 12
  requires :material, name: :tarromin

  rewards :product, name: :unfinished_potion_95
end

# Create Unfinished Harralander Potion
create_unfinished_potion :unfinished_harralander_potion do
  requires :skill, id: Skill::HERBLORE, level: 22
  requires :material, name: :harralander

  rewards :product, name: :unfinished_potion_97
end

# Create Unfinished Ranarr Potion
create_unfinished_potion :unfinished_harralander_potion do
  requires :skill, id: Skill::HERBLORE, level: 30
  requires :material, name: :ranarr_weed

  rewards :product, name: :unfinished_potion_99
end

# Create Unfinished Toadflax Potion
create_unfinished_potion :unfinished_toadflax_potion do
  requires :skill, id: Skill::HERBLORE, level: 34
  requires :material, name: :toadflax

  rewards :product, name: :unfinished_potion_3002
end

# Create Unfinished Irit Potion
create_unfinished_potion :unfinished_irit_potion do
  requires :skill, id: Skill::HERBLORE, level: 45
  requires :material, name: :irit_leaf

  rewards :product, name: :unfinished_potion_101
end

# Create Unfinished Avantoe Potion
create_unfinished_potion :unfinished_avantoe_potion do
  requires :skill, id: Skill::HERBLORE, level: 50
  requires :material, name: :avantoe

  rewards :product, name: :unfinished_potion_103
end

# Create Unfinished Kwaurm Potion
create_unfinished_potion :unfinished_kwuarm_potion do
  requires :skill, id: Skill::HERBLORE, level: 55
  requires :material, name: :kwuarm

  rewards :product, name: :unfinished_potion_105
end

# Create Unfinished Snapdragon Potion
create_unfinished_potion :unfinished_snapdragon_potion do
  requires :skill, id: Skill::HERBLORE, level: 63
  requires :material, name: :snapdragon

  rewards :product, name: :unfinished_potion_3004
end

# Create Unfinished Cadantine Potion
create_unfinished_potion :unfinished_snapdragon_potion do
  requires :skill, id: Skill::HERBLORE, level: 66
  requires :material, name: :cadantine

  rewards :product, name: :unfinished_potion_107
end

# Create Unfinished Lantadyme Potion
create_unfinished_potion :unfinished_snapdragon_potion do
  requires :skill, id: Skill::HERBLORE, level: 69
  requires :material, name: :lantadyme

  rewards :product, name: :unfinished_potion_2483
end

# Create Unfinished Dwarf Weed Potion
create_unfinished_potion :unfinished_snapdragon_potion do
  requires :skill, id: Skill::HERBLORE, level: 72
  requires :material, name: :dwarf_weed

  rewards :product, name: :unfinished_potion_109
end

# Create Unfinished Torstol Potion
create_unfinished_potion :unfinished_snapdragon_potion do
  requires :skill, id: Skill::HERBLORE, level: 78
  requires :material, name: :torstol

  rewards :product, name: :unfinished_potion_111
end