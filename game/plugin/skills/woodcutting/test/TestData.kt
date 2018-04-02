import org.apollo.game.plugin.skills.woodcutting.Tree

data class WoodcuttingTestData(val treeId: Int, val stumpId: Int, val tree: Tree)

fun woodcuttingTestData(): Collection<WoodcuttingTestData> = Tree.values()
    .flatMap { tree -> tree.objects.map { WoodcuttingTestData(it, tree.stump, tree) } }
    .toList()