package de.bennet_krause.nim.game

/**
 * Status of a nim game.
 */
enum class Status {

    /**
     * The game is not finished yet and someone can make a move.
     * Note: The game may already be decided while it has this status, as there may only be one possible move left to do.
     * However, technically the other player still has to perform this inevitable move so that the game is finished.
     */
    ONGOING,

    /**
     * The player won the game, no further move is possible until reset.
     */
    PLAYER_WON,

    /**
     * The computer won the game, no further move is possible until reset.
     */
    COMPUTER_WON
}