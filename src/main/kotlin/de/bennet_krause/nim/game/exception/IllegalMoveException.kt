package de.bennet_krause.nim.game.exception

/**
 * Thrown to indicate that a player tried to perform a move that violates the game rules.
 */
class IllegalMoveException(reason: String) : RuntimeException(reason)