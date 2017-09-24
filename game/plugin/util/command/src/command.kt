package org.apollo.game.plugin.util.command

import org.apollo.game.model.entity.Player

/**
 * Checks whether the amount of arguments provided is correct, sending the player the specified
 * message if not.
 */
fun valid_arg_length(args: Array<String>, length: IntRange, player: Player, message: String): Boolean {
    val valid = length.contains(args.size)
    if (!valid) {
        player.sendMessage(message)
    }
    return valid
}

/**
 * Checks whether the amount of arguments provided is correct, sending the player the specified
 * message if not.
 */
fun valid_arg_length(args: Array<String>, length: Int, player: Player, message: String)
    = valid_arg_length(args, IntRange(length, length), player, message)