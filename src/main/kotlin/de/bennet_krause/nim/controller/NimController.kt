package de.bennet_krause.nim.controller

import de.bennet_krause.nim.exception.NimError
import de.bennet_krause.nim.model.NimTurn
import de.bennet_krause.nim.model.NimState
import de.bennet_krause.nim.service.NimService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * REST controller for a nim game.
 */
@RestController
@RequestMapping("/nim")
@Tag(name = "Nim-Game", description = "API for a Nim game")
class NimController(

        /**
         * Service instance to control the nim game.
         */
        @field:Autowired
        private val nimService: NimService
) {

    /**
     * Handles a GET request to /state
     *
     * @return [NimState] current state of the game
     */
    @Operation(summary = "Current state of the game",
            description = "Returns information about the current state of the game.")
    @NimStateResponse
    @GetMapping("/state")
    fun getState(): NimState {
        return nimService.currentState()
    }

    /**
     * Handles a POST request to /move
     *
     * Takes a turn on behalf of the player, by taking away [NimTurn.nimCount] nims from the pile.
     * The computer will directly take its own turn afterwards, if possible.
     *
     * @param turn [NimTurn] object containing [NimTurn.nimCount], the number of nims to take in the move
     * @return [NimState] state of the game after both moves
     */
    @Operation(summary = "Takes a player turn",
            description = "Takes a turn on behalf of the player, by taking away nimCount nims from the pile. " +
                    "The computer will directly take its own turn afterwards, if possible. " +
                    "Returns information about the current state of the game after both turns.")
    @NimStateOrErrorResponse
    @PostMapping("/turn")
    fun postMove(@Valid @RequestBody turn: NimTurn): NimState {
        return nimService.takePlayerTurn(turn.nimCount!!)
    }

    /**
     * Handles a POST request to /reset
     *
     * Resets the game to its initial state.
     *
     * @return [NimState] initial state of the game
     */
    @Operation(summary = "Resets all state of the game",
            description = "Resets all state of the game, so that the player can start over.")
    @NimStateResponse
    @PostMapping("/reset")
    fun postReset(): NimState {
        return nimService.resetGame()
    }
}

/**
 * Declares a response with code 200 and json body containing [NimState].
 */
@ApiResponses(value = [
    ApiResponse(responseCode = "200", content = [Content(mediaType = "application/json", schema = Schema(implementation = NimState::class))])]
)
private annotation class NimStateResponse

/**
 * Declares a response with code 200 and json body containing [NimState],
 * and a response with code 400 and json body containing [NimError],
 */
@ApiResponses(value = [
    ApiResponse(responseCode = "200", content = [Content(mediaType = "application/json", schema = Schema(implementation = NimState::class))]),
    ApiResponse(responseCode = "400", content = [Content(mediaType = "application/json", schema = Schema(implementation = NimError::class))])]
)
private annotation class NimStateOrErrorResponse