
import assertk.Assert
import org.apollo.game.model.inv.Inventory
import org.assertj.core.api.Assertions.assertThat

fun Assert<Inventory>.contains(id: Int, amount: Int = 1) {
    assertThat(actual.getAmount(id)).`as`("amount of [$id]").isEqualTo(amount)
}