import org.apollo.game.model.entity.Player

/**
 * Checks whether the amount of arguments provided is correct, sending the player the specified
 * message if not.
 */
fun valid_arg_length(args: Array<String>, length: Any, player: Player, message: String): Boolean {
    val valid = when (length) {
        is Int -> length == args.size
        is IntRange -> length.contains(args.size)
        else -> {
            throw IllegalArgumentException("length must be one of the following: Int, IntRange")
        }
    }
    if (!valid) {
        player.sendMessage(message)
    }
    return valid
}