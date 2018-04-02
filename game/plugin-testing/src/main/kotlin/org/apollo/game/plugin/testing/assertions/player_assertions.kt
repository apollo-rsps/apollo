
import assertk.Assert
import org.apollo.game.model.entity.Player
import org.assertj.core.api.Assertions.assertThat

fun Assert<Player>.hasExperience(skill: Int, experience: Double) {
    assertThat(actual.skillSet.getExperience(skill)).`as`("experience in id: $skill").isEqualTo(experience)
}