package de.bennet_krause.nim.exception

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus

/**
 * Represents error information and a HTTP status that can be sent as a response in case of an exception in the controller.
 */
class NimError(

        /**
         * The HTTP status of the response.
         */
        @field:Schema(example = "BAD_REQUEST", required = true)
        val status: HttpStatus,

        /**
         * A message describing the error.
         */
        @field:Schema(description = "A message describing the error.", required = true)
        val message: String
)