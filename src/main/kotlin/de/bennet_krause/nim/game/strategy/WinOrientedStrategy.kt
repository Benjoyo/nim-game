package de.bennet_krause.nim.game.strategy

import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

/**
 * [NimStrategy] implementation that returns a number of nims to take so that the computer has a high chance of winning given the right conditions.
 */
class WinOrientedStrategy: NimStrategy {

    /**
     * Returns a number of nims to take in a win oriented game. Will not exceed maximum allowed number and never take more nims than there are left.
     * [currentPileSize] is the number of nims left on the pile at that moment,
     * [maxNimCount] specifies how many nims one is allowed to take according to the rules.
     *
     * @return number of nims to take
     */
    override fun calculateMove(currentPileSize: Int, maxNimCount: Int): Int {

        val move = max(1,((currentPileSize % (maxNimCount + 1)) + maxNimCount) % (maxNimCount + 1))

        return min(currentPileSize, move)
    }
}
