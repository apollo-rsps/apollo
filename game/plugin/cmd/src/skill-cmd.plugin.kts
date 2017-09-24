import com.google.common.primitives.Doubles
import com.google.common.primitives.Ints
import org.apollo.game.model.entity.Skill
import org.apollo.game.model.entity.SkillSet
import org.apollo.game.model.entity.setting.PrivilegeLevel

/**
 * Maximises the player's skill set.
 */
on_command("max", PrivilegeLevel.ADMINISTRATOR)
    .then { player ->
        val skills = player.skillSet

        for (skill in 0 until skills.size()) {
            skills.addExperience(skill, SkillSet.MAXIMUM_EXP)
        }
    }

/**
 * Levels the specified skill to the specified level, optionally updating the current level as well.
 */
on_command("level", PrivilegeLevel.ADMINISTRATOR)
    .then { player ->
        val invalidSyntax = "Invalid syntax - ::level [skill-id] [level] <old>"
        if (arguments.size !in 2..3) {
            player.sendMessage(invalidSyntax)
            return@then
        }

        val skillId = Ints.tryParse(arguments[0])
        if (skillId == null) {
            player.sendMessage(invalidSyntax)
            return@then
        }
        val level = Ints.tryParse(arguments[1])
        if (level == null) {
            player.sendMessage(invalidSyntax)
            return@then
        }

        if (skillId !in 0..20 || level !in 1..99) {
            player.sendMessage(invalidSyntax)
            return@then
        }

        val experience = SkillSet.getExperienceForLevel(level).toDouble()
        var current = level

        if (arguments.size == 3 && arguments[2] == "old") {
            val skill = player.skillSet.getSkill(skillId)
            current = skill.currentLevel
        }

        player.skillSet.setSkill(skillId, Skill(experience, current, level))
    }

/**
 * Adds the specified amount of experience to the specified skill.
 */
on_command("xp", PrivilegeLevel.ADMINISTRATOR)
    .then { player ->
        val invalidSyntax = "Invalid syntax - ::xp [skill-id] [experience]"
        if (arguments.size != 2) {
            player.sendMessage(invalidSyntax)
            return@then
        }

        val skillId = Ints.tryParse(arguments[0])
        if (skillId == null) {
            player.sendMessage(invalidSyntax)
            return@then
        }
        val experience = Doubles.tryParse(arguments[1])
        if (experience == null) {
            player.sendMessage(invalidSyntax)
            return@then
        }

        if (skillId !in 0..20 || experience <= 0) {
            player.sendMessage("Invalid syntax - ::xp [skill-id] [experience]")
            return@then
        }

        player.skillSet.addExperience(skillId, experience)
    }