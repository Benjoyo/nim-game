package de.bennet_krause.nim.game

import de.bennet_krause.nim.game.exception.IllegalMoveException
import de.bennet_krause.nim.game.exception.WrongTurnException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class NimGameTest {

    private lateinit var nimGame: NimGame

    @BeforeEach
    fun setUp() {
        nimGame = NimGame()
    }

    @Test
    fun `should have full pile initially`() {
        assertEquals(NimGame.INITIAL_PILE_SIZE, nimGame.currentPileSize)
    }

    @Test
    fun `should not be finished initially`() {
        assertEquals(Status.ONGOING, nimGame.checkGameStatus())
    }

    @Test
    fun `should be the player's turn initially`() {
        assertTrue(nimGame.isPlayersTurn)
    }

    @Test
    fun `should throw IllegalMoveException when number of nims in a move is 0, pile should remain untouched`() {
        assertThrows(IllegalMoveException::class.java) {
            nimGame.takePlayerTurn(0)
        }
        assertEquals(NimGame.INITIAL_PILE_SIZE, nimGame.currentPileSize)
    }

    @Test
    fun `should throw IllegalMoveException when number of nims in a move is negative, pile should remain untouched`() {
        assertThrows(IllegalMoveException::class.java) {
            nimGame.takePlayerTurn(-3)
        }
        assertEquals(NimGame.INITIAL_PILE_SIZE, nimGame.currentPileSize)
    }

    @Test
    fun `should throw IllegalMoveException when number of nims in a move is above 3, pile should remain untouched`() {
        assertThrows(IllegalMoveException::class.java) {
            nimGame.takePlayerTurn(4)
        }
        assertEquals(NimGame.INITIAL_PILE_SIZE, nimGame.currentPileSize)
    }

    @Test
    fun `should not throw Exception when number of nims in a move is 1`() {
        assertDoesNotThrow {
            nimGame.takePlayerTurn(1)
        }
        assertEquals(12, nimGame.currentPileSize)
    }

    @Test
    fun `should not throw Exception when number of nims in a move is 2`() {
        assertDoesNotThrow {
            nimGame.takePlayerTurn(2)
        }
        assertEquals(11, nimGame.currentPileSize)
    }

    @Test
    fun `should not throw Exception when number of nims in a move is 3`() {
        assertDoesNotThrow {
            nimGame.takePlayerTurn(3)
        }
        assertEquals(10, nimGame.currentPileSize)
    }

    @Test
    fun `pile should decrease by 1 when number of nims in a move is 1`() {
        nimGame.takePlayerTurn(1)

        assertEquals(12, nimGame.currentPileSize)
    }

    @Test
    fun `pile should decrease by 2 when number of nims in a move is 2`() {
        nimGame.takePlayerTurn(2)

        assertEquals(11, nimGame.currentPileSize)
    }

    @Test
    fun `pile should decrease by 3 when number of nims in a move is 3`() {
        nimGame.takePlayerTurn(3)

        assertEquals(10, nimGame.currentPileSize)
    }

    @Test
    fun `should throw IllegalMoveException when number of nims in a move is bigger than remaining pile`() {

        setCurrentNimPileSize(2)

        assertThrows(IllegalMoveException::class.java) {
            nimGame.takePlayerTurn(3)
        }
    }

    @Test
    fun `should not throw IllegalMoveException when number of nims in a move is equal to remaining pile`() {

        setCurrentNimPileSize(3)

        assertDoesNotThrow {
            nimGame.takePlayerTurn(3)
        }
    }

    @Test
    fun `when both player and computer take a turn, the pile should be decreased accordingly (1)`() {
        nimGame.takePlayerTurn(3)
        nimGame.takeComputerTurn()

        assertEquals(10 - nimGame.lastComputerMove, nimGame.currentPileSize)
    }

    @Test
    fun `when both player and computer take a turn, the pile should be decreased accordingly (2)`() {
        nimGame.takePlayerTurn(3)
        nimGame.takeComputerTurn()
        nimGame.takePlayerTurn(2)

        assertEquals(8 - nimGame.lastComputerMove, nimGame.currentPileSize)
    }

    @Test
    fun `after a player turn, it should not be his turn anymore (1)`() {
        nimGame.takePlayerTurn(1)

        assertFalse(nimGame.isPlayersTurn)
    }

    @Test
    fun `after a player turn, it should not be his turn anymore (2)`() {
        nimGame.takePlayerTurn(2)
        nimGame.takeComputerTurn()
        nimGame.takePlayerTurn(3)

        assertFalse(nimGame.isPlayersTurn)
    }

    @Test
    fun `after a computer turn, it should be the players turn`() {
        nimGame.takePlayerTurn(1)
        nimGame.takeComputerTurn()

        assertTrue(nimGame.isPlayersTurn)
    }

    @Test
    fun `after two turns, the game should still be ongoing`() {
        nimGame.takePlayerTurn(3)
        nimGame.takeComputerTurn()

        assertEquals(Status.ONGOING, nimGame.checkGameStatus())
    }

    @Test
    fun `after three turns, the game should still be ongoing`() {
        nimGame.takePlayerTurn(3)
        nimGame.takeComputerTurn()
        nimGame.takePlayerTurn(2)

        assertEquals(Status.ONGOING, nimGame.checkGameStatus())
    }

    @Test
    fun `when the player takes the last nims, he should lose and the pile should be emtpy`() {

        setCurrentNimPileSize(3)

        nimGame.takePlayerTurn(3)

        assertEquals(Status.COMPUTER_WON, nimGame.checkGameStatus())
        assertEquals(0, nimGame.currentPileSize)
    }

    @Test
    fun `when the player takes the last but one nim, he should win`() {

        setCurrentNimPileSize(3)

        nimGame.takePlayerTurn(2)
        nimGame.takeComputerTurn()

        assertEquals(Status.PLAYER_WON, nimGame.checkGameStatus())
        assertEquals(1, nimGame.lastComputerMove)
        assertEquals(0, nimGame.currentPileSize)
    }

    @Test
    fun `should throw WrongTurnException when computer starts`() {
        assertThrows(WrongTurnException::class.java) {
            nimGame.takeComputerTurn()
        }
        assertEquals(NimGame.INITIAL_PILE_SIZE, nimGame.currentPileSize)
    }

    @Test
    fun `should throw WrongTurnException when player tries two turns in a row`() {
        nimGame.takePlayerTurn(1)

        assertThrows(WrongTurnException::class.java) {
            nimGame.takePlayerTurn(1)
        }
    }

    @Test
    fun `should throw WrongTurnException when computer tries two turns in a row`() {
        nimGame.takePlayerTurn(1)
        nimGame.takeComputerTurn()

        assertThrows(WrongTurnException::class.java) {
            nimGame.takeComputerTurn()
        }
    }

    @Test
    fun `reset should reset all state (1)`() {
        nimGame.reset()

        assertEquals(NimGame.INITIAL_PILE_SIZE, nimGame.currentPileSize)
        assertEquals(0, nimGame.lastComputerMove)
        assertTrue(nimGame.isPlayersTurn)
    }

    @Test
    fun `reset should reset all state (2)`() {
        nimGame.takePlayerTurn(3)
        nimGame.takeComputerTurn()
        nimGame.takePlayerTurn(1)

        nimGame.reset()

        assertEquals(NimGame.INITIAL_PILE_SIZE, nimGame.currentPileSize)
        assertEquals(0, nimGame.lastComputerMove)
        assertTrue(nimGame.isPlayersTurn)
    }

    /*
     * Helpers
     */

    private fun setCurrentNimPileSize(size: Int) {
        val currentPileSize = NimGame::class.java.getDeclaredField(NimGame::currentPileSize.name)
        currentPileSize.isAccessible = true
        currentPileSize.set(nimGame, size)
    }
}