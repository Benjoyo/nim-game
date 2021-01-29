package de.bennet_krause.nim.game.strategy

/**
 * Interface for a concrete computer strategy in the nim game.
 */
interface NimStrategy {

    /**
     * Returns the number of nims that the computer should take in the next move.
     * [currentPileSize] is the number of nims left on the pile at that moment,
     * [maxNimCount] specifies how many nims one is allowed to take according to the rules.
     */
    fun calculateMove(currentPileSize: Int, maxNimCount: Int): Int
}