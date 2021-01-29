package de.bennet_krause.nim.game

import de.bennet_krause.nim.game.exception.IllegalMoveException
import de.bennet_krause.nim.game.exception.WrongTurnException
import de.bennet_krause.nim.game.strategy.NimStrategy
import de.bennet_krause.nim.game.strategy.RandomStrategy
import de.bennet_krause.nim.game.strategy.WinOrientedStrategy
import org.slf4j.LoggerFactory

class NimGame(

        // initial size of the nim pile
        var initialPileSize: Int = DEFAULT_INITIAL_PILE_SIZE,

        // maximum number of nims one can take
        var maxNimCount: Int = DEFAULT_MAX_NIM_COUNT

) {

    companion object {
        internal const val DEFAULT_INITIAL_PILE_SIZE = 13
        internal const val DEFAULT_MAX_NIM_COUNT = 3
    }

    private val log = LoggerFactory.getLogger(NimGame::class.java)

    // the strategy that the computer is following
    var computerStrategy: NimStrategy = WinOrientedStrategy()

    // last move that the computer did
    var lastComputerMove: Int = 0
        private set

    // current number of nims on the pile, initially 13
    var currentPileSize: Int = initialPileSize
        private set

    // true if it's the turn of the player, false if it's the turn of the computer
    var isPlayersTurn: Boolean = true
        private set


    /**
     * Takes a turn for the player. [nimCount] is the number of nims that the player wants to take in his move.
     *
     * @return [Status] the current status of the game.
     *
     * @throws WrongTurnException if it is the computers turn
     * @throws IllegalMoveException if [nimCount] is not in compliance with the game rules (too small or too large)
     */
    fun takePlayerTurn(nimCount: Int): Status {
        if (!isPlayersTurn) {
            // it is the computers turn!
            throw WrongTurnException()
        }

        // take the requested turn
        takeTurn(nimCount)

        // check if game is finished (pile is empty), in that case the computer won
        return checkGameStatus()
    }

    /**
     * Takes a turn for the computer. The number of nims that the computer takes is determined automatically.
     *
     * @return [Status] the current status of the game.
     *
     * @throws WrongTurnException if it is the players turn
     */
    fun takeComputerTurn(): Status {
        if (isPlayersTurn) {
            // it is the players turn!
            throw WrongTurnException()
        }

        // take the computer turn according to some strategy
        val nimCount = computerStrategy.calculateMove(currentPileSize, maxNimCount)
        takeTurn(nimCount)

        // remember the last move of the computer, so that the player may know what happened
        lastComputerMove = nimCount

        // check if game is finished (pile is empty), in that case the player won
        return checkGameStatus()
    }

    /**
     * Actually take a turn. [nimCount] is the number of nims that are taken off the pile.
     *
     * @throws IllegalMoveException if [nimCount] is not in compliance with the game rules (too small or too large)
     */
    private fun takeTurn(nimCount: Int) {

        // verify that nimCount is within the bounds of the game rules
        if (nimCount !in (1..maxNimCount)) {
            throw IllegalMoveException("Player tried to take $nimCount nims while only 1 to $maxNimCount nims are allowed.")
        }

        // verify that there are enough nims left on the pile to take the turn
        if (nimCount > currentPileSize) {
            throw IllegalMoveException("Player tried to take $nimCount nims while only $currentPileSize nims are left on the pile.")
        }

        // take the requested number of nims from the pile
        currentPileSize -= nimCount

        log.info("${if (isPlayersTurn) "Player" else "Computer"} took $nimCount nims. Nims left: $currentPileSize")

        // it's the other player's turn now
        isPlayersTurn = !isPlayersTurn
    }

    /**
     * Check whether the game is still ongoing or if someone has won.
     *
     * @return [Status] the current status of the game.
     */
    fun checkGameStatus(): Status {
        return if (currentPileSize == 0) {
            // if the pile is empty, the player whose turn it would be next has won
            if (isPlayersTurn) {
                Status.PLAYER_WON
            } else {
                Status.COMPUTER_WON
            }
        } else {
            // otherwise the game continues
            Status.ONGOING
        }
    }

    /**
     * Sets the current state of the game.
     */
    fun setState(isPlayersTurn: Boolean, currentPileSize: Int, lastComputerMove: Int) {
        this.isPlayersTurn = isPlayersTurn
        this.currentPileSize = currentPileSize
        this.lastComputerMove = lastComputerMove
    }

    /**
     * Reset nim game to initial state.
     */
    fun reset() {
        lastComputerMove = 0
        currentPileSize = initialPileSize
        isPlayersTurn = true
    }
}