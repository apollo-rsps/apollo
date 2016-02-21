java_import 'org.apollo.cache.def.ItemDefinition'

#-----------------Herblore Helper Methods-----------------

# Example wrapper method.
def create_finished_potion(name, amount = 1, &block)
  recipe = create_recipe name, amount do
    message :success, "You make the potion." # Todo change message

    set_animation 363
    set_action_type :repeatable
    
    self.instance_eval(&block)
    
    register_to_skill_menu(Skill::HERBLORE, "Potions")
  end
end

#-----------------Start of making finished potions-----------------

# Create Attack Potion
create_finished_potion :attack_potion_121 do
  requires :skill, id: Skill::HERBLORE, level: 1

  requires :material, name: :eye_of_newt
  requires :material, name: :unfinished_potion_91

  rewards :experience, id: Skill::HERBLORE, amount: 25
end

# Create Attack Potion
create_finished_potion :antipoison_175 do
  requires :skill, id: Skill::HERBLORE, level: 5

  requires :material, name: :unicorn_horn_dust
  requires :material, name: :unfinished_potion_93

  rewards :experience, id: Skill::HERBLORE, amount: 37.5
end

# Create Strength Potion
create_finished_potion :srength_potion_115 do
  requires :skill, id: Skill::HERBLORE, level: 12

  requires :material, name: :limpwurt_root
  requires :material, name: :unfinished_potion_95

  rewards :experience, id: Skill::HERBLORE, amount: 50
end

# Create Restore Potion
create_finished_potion :restore_potion_127 do
  requires :skill, id: Skill::HERBLORE, level: 18

  requires :material, name: :red_spiders_eggs_223
  requires :material, name: :unfinished_potion_97

  rewards :experience, id: Skill::HERBLORE, amount: 62.5
end

# Create Energy Potion
create_finished_potion :energy_potion_3010 do
  requires :skill, id: Skill::HERBLORE, level: 26

  requires :material, name: :chocolate_dust
  requires :material, name: :unfinished_potion_97

  rewards :experience, id: Skill::HERBLORE, amount: 67.5
end

# Create Defence Potion
create_finished_potion :defence_potion_133 do
  requires :skill, id: Skill::HERBLORE, level: 30

  requires :material, name: :white_berries
  requires :material, name: :unfinished_potion_99

  rewards :experience, id: Skill::HERBLORE, amount: 75
end

# Create Agility Potion
create_finished_potion :agility_potion_3034 do
  requires :skill, id: Skill::HERBLORE, level: 34

  requires :material, name: :toads_legs_2152
  requires :material, name: :unfinished_potion_3002

  rewards :experience, id: Skill::HERBLORE, amount: 80
end

# Create Prayer Potion
create_finished_potion :prayer_potion_139 do
  requires :skill, id: Skill::HERBLORE, level: 38

  requires :material, name: :snape_grass
  requires :material, name: :unfinished_potion_99

  rewards :experience, id: Skill::HERBLORE, amount: 87.5
end

# Create Super Attack Potion
create_finished_potion :super_attack_145 do
  requires :skill, id: Skill::HERBLORE, level: 45

  requires :material, name: :eye_of_newt
  requires :material, name: :unfinished_potion_101

  rewards :experience, id: Skill::HERBLORE, amount: 100
end

# Create Super Anti Potion
create_finished_potion :super_attack_181 do
  requires :skill, id: Skill::HERBLORE, level: 48

  requires :material, name: :unicorn_horn_dust
  requires :material, name: :unfinished_potion_101

  rewards :experience, id: Skill::HERBLORE, amount: 106.3
end

# Create Fishing Potion
create_finished_potion :fishing_potion_151 do
  requires :skill, id: Skill::HERBLORE, level: 50

  requires :material, name: :snape_grass
  requires :material, name: :unfinished_potion_103

  rewards :experience, id: Skill::HERBLORE, amount: 112.5
end

# Create Super Energy Potion
create_finished_potion :super_energy_3018 do
  requires :skill, id: Skill::HERBLORE, level: 52

  requires :material, name: :mort_myre_fungi
  requires :material, name: :unfinished_potion_103

  rewards :experience, id: Skill::HERBLORE, amount: 117.5
end

# Create Super Strength Potion
create_finished_potion :super_strength_157 do
  requires :skill, id: Skill::HERBLORE, level: 55

  requires :material, name: :dragon_scale_dust
  requires :material, name: :unfinished_potion_105

  rewards :experience, id: Skill::HERBLORE, amount: 125
end

# Create Weapon Poision Potion
create_finished_potion :weapon_poision_187 do
  requires :skill, id: Skill::HERBLORE, level: 60

  requires :material, name: :dragon_scale_dust
  requires :material, name: :unfinished_potion_105

  rewards :experience, id: Skill::HERBLORE, amount: 137.5
end

# Create Super Restore Potion
create_finished_potion :super_restore_3026 do
  requires :skill, id: Skill::HERBLORE, level: 63

  requires :material, name: :red_spiders_eggs_223
  requires :material, name: :unfinished_potion_3004

  rewards :experience, id: Skill::HERBLORE, amount: 142.5
end

# Create Super Defence Potion
create_finished_potion :super_defence_163 do
  requires :skill, id: Skill::HERBLORE, level: 66

  requires :material, name: :white_berries
  requires :material, name: :unfinished_potion_107

  rewards :experience, id: Skill::HERBLORE, amount: 150
end

# Create Antifire Potion
create_finished_potion :antifire_potion_2428 do
  requires :skill, id: Skill::HERBLORE, level: 69

  requires :material, name: :dragon_scale_dust
  requires :material, name: :unfinished_potion_2483

  rewards :experience, id: Skill::HERBLORE, amount: 157.5
end

# Create Ranging Potion
create_finished_potion :ranging_potion_169 do
  requires :skill, id: Skill::HERBLORE, level: 72

  requires :material, name: :wine_of_zamorak
  requires :material, name: :unfinished_potion_109

  rewards :experience, id: Skill::HERBLORE, amount: 162.5
end

# Create Magic Potion
create_finished_potion :magic_potion_3042 do
  requires :skill, id: Skill::HERBLORE, level: 76

  requires :material, name: :potato_cactus
  requires :material, name: :unfinished_potion_2483

  rewards :experience, id: Skill::HERBLORE, amount: 172.5
end

# Create Zamorak Brew
create_finished_potion :zamorak_brew_169 do
  requires :skill, id: Skill::HERBLORE, level: 78

  requires :material, name: :jangerberries
  requires :material, name: :unfinished_potion_111

  rewards :experience, id: Skill::HERBLORE, amount: 175
end