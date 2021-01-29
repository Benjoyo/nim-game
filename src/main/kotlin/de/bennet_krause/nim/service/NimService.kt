package de.bennet_krause.nim.service

import de.bennet_krause.nim.game.NimGame
import de.bennet_krause.nim.game.Status
import de.bennet_krause.nim.model.NimConfig
import de.bennet_krause.nim.model.NimState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Service to control a nim game from the player's perspective.
 */
@Service
class NimService(

        /**
         * The actual nim game instance.
         */
        @field:Autowired
        private val nimGame: NimGame
) {

    /**
     * Returns the current [NimState] of the game.
     * Contains the nims left on the pile, the status of the game and the last move of the computer.
     */
    fun currentState(): NimState {
        return NimState(
                nimGame.currentPileSize,
                nimGame.checkGameStatus(),
                nimGame.maxNimCount,
                nimGame.lastComputerMove
        )
    }

    /**
     * Takes a player turn by taking [nimCount] nims off the pile.
     * Directly afterwards the computer takes its turn, when possible.
     * The state after both turns is returned.
     */
    fun takePlayerTurn(nimCount: Int): NimState {
        // take player turn
        val status = nimGame.takePlayerTurn(nimCount)
        // if the move didn't end the game, take the computer's turn as well
        if (status == Status.ONGOING) {
            nimGame.takeComputerTurn()
        }
        // return result of both moves
        return currentState()
    }

    /**
     * Resets the game to its initial state.
     */
    fun resetGame(): NimState {
        nimGame.reset()
        return currentState()
    }

    /**
     * Configures the game according to the given [NimConfig].
     */
    fun configureGame(config: NimConfig) {
        nimGame.initialPileSize = config.initialPileSize!!
        nimGame.maxNimCount = config.maxNimCount!!
    }
}

