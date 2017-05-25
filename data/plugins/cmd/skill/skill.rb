require 'java'
java_import 'org.apollo.game.model.entity.SkillSet'
java_import 'org.apollo.game.model.entity.Skill'

# Maximises the player's skill set.
on :command, :max, RIGHTS_ADMIN do |player, _command|
  skills = player.skill_set

  (0...skills.size).each do |skill|
    skills.add_experience(skill, SkillSet::MAXIMUM_EXP)
  end
end

# Levels the specified skill to the specified level, optionally updating the current level as well.
on :command, :level, RIGHTS_ADMIN do |player, command|
  args = command.arguments
  unless (2..3).include?(args.length) && (0..20).include?(skill_id = args[0].to_i) &&
         (1..99).include?(level = args[1].to_i)
    player.send_message('Invalid syntax - ::level [skill-id] [level]')
    next
  end

  experience = SkillSet.get_experience_for_level(level)
  current = level

  if args.length == 3 && args[2].to_s == 'old'
    skill = player.skill_set.skill(skill_id)
    current = skill.current_level
  end

  player.skill_set.set_skill(skill_id, Skill.new(experience, current, level))
end

# Adds the specified amount of experience to the specified skill.
on :command, :xp, RIGHTS_ADMIN do |player, command|
  args = command.arguments
  unless args.length == 2 && (0..20).include?(skill_id = args[0].to_i) &&
         (experience = args[1].to_i) >= 0
    player.send_message('Invalid syntax - ::xp [skill-id] [experience]')
    return
  end

  player.skill_set.add_experience(skill_id, experience)
end
