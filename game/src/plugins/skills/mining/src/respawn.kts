


fun respawn_pulses(base: Int, players: Int) {
    return base - players * base / (world.playerRepository.size() * 2);
}