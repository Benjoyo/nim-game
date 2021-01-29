package de.bennet_krause.nim.service

import de.bennet_krause.nim.game.NimGame
import de.bennet_krause.nim.game.Status
import de.bennet_krause.nim.model.NimConfig
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
internal class NimServiceTest {

    @Mock
    private lateinit var nimGame: NimGame

    @InjectMocks
    private lateinit var nimService: NimService

    @BeforeEach
    fun setUp() {
        `when`(nimGame.checkGameStatus()).thenReturn(Status.ONGOING)
        `when`(nimGame.takePlayerTurn(anyInt())).thenReturn(Status.ONGOING)
        `when`(nimGame.takeComputerTurn()).thenReturn(Status.ONGOING)
    }


    @Test
    fun `currentState gets information from nim game`() {
        nimService.currentState()

        verify(nimGame).checkGameStatus()
        verify(nimGame).currentPileSize
        verify(nimGame).lastComputerMove
    }

    @Test
    fun `takePlayerTurn calls takePlayerTurn and takeComputerTurn on nim game`() {
        nimService.takePlayerTurn(3)

        verify(nimGame).takePlayerTurn(eq(3))
        verify(nimGame).takeComputerTurn()
    }

    @Test
    fun `takePlayerTurn does not call takeComputerTurn if player finished game`() {
        `when`(nimGame.takePlayerTurn(anyInt())).thenReturn(Status.COMPUTER_WON)

        nimService.takePlayerTurn(3)

        verify(nimGame).takePlayerTurn(eq(3))
        verify(nimGame, never()).takeComputerTurn()
    }

    @Test
    fun `resetGame calls reset on nim game`() {
        nimService.resetGame()

        verify(nimGame).reset()
    }

    @Test
    fun `configureGame configures nim game`() {
        val config = NimConfig(10, 5)

        nimService.configureGame(config)

        verify(nimGame).initialPileSize = 10
        verify(nimGame).maxNimCount = 5
    }
}