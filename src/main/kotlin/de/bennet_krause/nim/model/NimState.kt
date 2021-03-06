package de.bennet_krause.nim.model

import de.bennet_krause.nim.game.Status
import io.swagger.v3.oas.annotations.media.Schema

/**
 * The current state of the game.
 */
class NimState(

        /**
         * The number of nims that is left on the pile.
         */
        @field:Schema(description = "The number of nims that is left on the pile", example = "10", required = true)
        val pile: Int,

        /**
         * Current status of the game (is it still ongoing or did someone win?)
         */
        @field:Schema(description = "Current status of the game (is it still ongoing or did someone win?)", required = true)
        val status: Status,

        /**
         * The number of nims one is allowed to take in a turn.
         */
        @field:Schema(description = "The number of nims one is allowed to take in a turn", example = "3", required = true)
        val maxNimCount: Int,

        /**
         * The number of nims the computer took in its last turn.
         */
        @field:Schema(description = "The number of nims the computer took in its last turn", example = "2", required = true)
        val lastComputerMove: Int
)