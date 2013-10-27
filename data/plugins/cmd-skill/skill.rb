require 'java'
java_import 'org.apollo.game.model.SkillSet'

on :command, :max, RIGHTS_ADMIN do |player, command|
  skills = player.skill_set
  (0...skills.size).each do |skill|
    skills.add_experience(skill, SkillSet::MAXIMUM_EXP)
  end
end
