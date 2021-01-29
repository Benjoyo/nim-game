package de.bennet_krause.nim.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
class NimPersistence(

        /**
         * Initial number of nims on the pile. Must be greater than 1.
         */
        var initialPileSize: Int = 0,

        /**
         * Maximum number of nims the player can to take in a turn. Must be greater than 1.
         */
        var maxNimCount: Int = 0,

        // last move that the computer did
        var lastComputerMove: Int = 0,

        // current number of nims on the pile
        var currentPileSize: Int = 0,

        // true if it's the turn of the player, false if it's the turn of the computer
        var isPlayersTurn: Boolean = false

) {
        companion object {
                const val ID = 1
        }

        @Id
        val id: Int = ID
}