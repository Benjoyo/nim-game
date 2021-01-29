package de.bennet_krause.nim.model

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

/**
 * Describes a turn that the player wants to take.
 */
class NimTurn(

        /**
         * Number of nims the player wants to take in his turn. Must be greater or equal 1.
         */
        @field:NotNull
        @field:Min(1)
        @field:Schema(description = "Number of nims the player wants to take in his turn", example = "3", required = true)
        val nimCount: Int?
)