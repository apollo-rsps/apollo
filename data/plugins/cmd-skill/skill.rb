require 'java'
java_import 'org.apollo.game.model.SkillSet'
java_import 'org.apollo.game.model.Skill'

on :command, :max, RIGHTS_ADMIN do |player, command|
  skills = player.skill_set
  (0...skills.size).each do |skill|
    skills.add_experience(skill, SkillSet::MAXIMUM_EXP)
  end
end

on :command, :level, RIGHTS_ADMIN do |player, command|
  args = command.arguments
  unless args.length == 2
    player.send_message("Invalid syntax - ::level [skill id] [level]")
    return
  end

  skill = args[0].to_i
  level = args[1].to_i

  unless (0..20).include? skill and (1..99).include? level
    player.send_message("Invalid syntax - ::level [skill id] [level]") 
    return
  end

  experience = SkillSet.experience_for_level(level)
  player.skill_set.set_skill(skill, Skill.new(experience, level, level))
end

on :command, :xp, RIGHTS_ADMIN do |player, command|
  args = command.arguments
  unless args.length == 2
    player.send_message("Invalid syntax - ::xp [skill id] [experience]")
    return
  end

  skill = args[0].to_i
  experience = args[1].to_i

  unless (0..20).include? skill
    player.send_message("Invalid syntax - ::xp [skill id] [experience]") 
    return
  end
  
  player.skill_set.add_experience(skill, experience)
end