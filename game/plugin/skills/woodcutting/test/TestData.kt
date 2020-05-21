import java.util.stream.Stream
import org.apollo.game.plugin.skills.woodcutting.Tree
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider

data class WoodcuttingTestData(val treeId: Int, val stumpId: Int, val tree: Tree)

fun woodcuttingTestData(): Collection<WoodcuttingTestData> = Tree.values()
    .flatMap { tree -> tree.objects.map { WoodcuttingTestData(it, tree.stump, tree) } }
    .toList()

class WoodcuttingTestDataProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        return woodcuttingTestData().map { Arguments { arrayOf(it) } }.stream()
    }
}