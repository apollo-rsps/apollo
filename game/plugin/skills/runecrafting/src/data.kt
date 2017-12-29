import org.apollo.game.model.Position

enum class Alter(val id: Int, val crafting: Int, val portal: Int, val entrance: Position, val exit: Position, val center: Position) {
    AIR_ALTER(2452, 2478, 2465, Position(2841, 4829), Position(2983, 3292), Position(2844, 4834)),
    MIND_ALTER(2453, 2479, 2466, Position(2793, 4828), Position(2980, 3514), Position(2786, 4841))
}

enum class Rune(val id: Int, val alter: Alter, val level: Int, val reward: Float, val bonus: Runnable) {

}