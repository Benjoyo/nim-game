package de.bennet_krause.nim.model

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

/**
 * Configures the game parameters.
 */
class NimConfig(

        /**
         * Initial number of nims on the pile. Must be greater than 1.
         */
        @field:NotNull
        @field:Min(2)
        @field:Schema(description = "Initial number of nims on the pile", example = "13", required = true)
        val initialPileSize: Int?,

        /**
         * Maximum number of nims the player can to take in a turn. Must be greater than 1.
         */
        @field:NotNull
        @field:Min(2)
        @field:Schema(description = "Maximum number of nims the player can to take in a turn", example = "3", required = true)
        val maxNimCount: Int?

)