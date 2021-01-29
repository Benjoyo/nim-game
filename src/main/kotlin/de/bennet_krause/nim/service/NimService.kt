package de.bennet_krause.nim.service

import de.bennet_krause.nim.game.NimGame
import de.bennet_krause.nim.game.Status
import de.bennet_krause.nim.model.NimConfig
import de.bennet_krause.nim.model.NimPersistence
import de.bennet_krause.nim.model.NimState
import de.bennet_krause.nim.repository.NimPersistenceRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Service to control a nim game from the player's perspective.
 */
@Service
class NimService(

        /**
         * Repository for persistent game state.
         */
        @field:Autowired
        private val nimPersistenceRepository: NimPersistenceRepository
) {

    private val log = LoggerFactory.getLogger(NimService::class.java)

    /**
     * The actual nim game instance. Either initialized from the database or created fresh.
     */
    internal lateinit var nimGame: NimGame

    /**
     * Get game state from database or create a new game.
     */
    init {
        val persistenceOptional = nimPersistenceRepository.findById(NimPersistence.ID)
        nimGame = if (persistenceOptional.isPresent) {
            log.info("Load persisted game state")
            // restore persisted game state
            val persistence = persistenceOptional.get()
            val game = NimGame(persistence.initialPileSize, persistence.maxNimCount)
            game.setState(persistence.isPlayersTurn, persistence.currentPileSize, persistence.lastComputerMove)
            game
        } else {
            log.info("No persisted game state, create new game")
            // create new default game as there is no previous configuration and state
            NimGame()
        }
    }


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
        persistGame()
        // return result of both moves
        return currentState()
    }

    /**
     * Resets the game to its initial state.
     */
    fun resetGame(): NimState {
        nimGame.reset()
        persistGame()
        return currentState()
    }

    /**
     * Configures the game according to the given [NimConfig].
     */
    fun configureGame(config: NimConfig) {
        nimGame.initialPileSize = config.initialPileSize!!
        nimGame.maxNimCount = config.maxNimCount!!
        persistGame()
    }

    private fun persistGame() {

        log.info("Persist game state")

        val persistence = nimPersistenceRepository.findById(NimPersistence.ID).orElse(NimPersistence())

        // state of running game
        persistence.currentPileSize = nimGame.currentPileSize
        persistence.isPlayersTurn = nimGame.isPlayersTurn
        persistence.lastComputerMove = nimGame.lastComputerMove

        // configuration
        persistence.initialPileSize = nimGame.initialPileSize
        persistence.maxNimCount = nimGame.maxNimCount

        // save
        nimPersistenceRepository.save(persistence)
    }
}

