package de.bennet_krause.nim.game.strategy

import kotlin.math.min
import kotlin.random.Random

/**
 * [NimStrategy] implementation that just returns a random number of nims to take.
 */
class RandomStrategy: NimStrategy {

    /**
     * Returns a random number of nims. Will not exceed maximum allowed number and never take more nims than there are left.
     * [currentPileSize] is the number of nims left on the pile at that moment,
     * [maxNimCount] specifies how many nims one is allowed to take according to the rules.
     *
     * @return random number of nims that are a valid move
     */
    override fun calculateMove(currentPileSize: Int, maxNimCount: Int): Int {
        return min(currentPileSize, Random.nextInt(1, maxNimCount))
    }
}