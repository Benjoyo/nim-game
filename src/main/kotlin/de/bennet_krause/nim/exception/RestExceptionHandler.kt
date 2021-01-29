package de.bennet_krause.nim.exception

import de.bennet_krause.nim.game.exception.IllegalMoveException
import de.bennet_krause.nim.game.exception.WrongTurnException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * Exception handler for all controllers. Returns a [NimError] describing the error.
 */
@ControllerAdvice
class RestExceptionHandler {

    /**
     * Exception handler for [IllegalMoveException] exception.
     */
    @ExceptionHandler(IllegalMoveException::class)
    fun handleIllegalMoveException(ex: IllegalMoveException): ResponseEntity<Any> {
        val error = NimError(HttpStatus.BAD_REQUEST, ex.message ?: "Illegal move")
        return buildResponseEntity(error)
    }

    /**
     * Exception handler for [WrongTurnException] exception.
     */
    @ExceptionHandler(WrongTurnException::class)
    fun handleWrongTurnException(ex: WrongTurnException): ResponseEntity<Any> {
        val error = NimError(HttpStatus.BAD_REQUEST, "Wrong turn")
        return buildResponseEntity(error)
    }

    /**
     * Exception handler for [MethodArgumentNotValidException] exception (e.g. when a request field validation failed).
     */
    @ExceptionHandler
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<Any> {
        val error = NimError(HttpStatus.BAD_REQUEST, "Request field(s) " + ex.fieldErrors.joinToString(", ") { it.field } + " not valid")
        return buildResponseEntity(error)
    }

    /**
     * Exception handler for other exceptions.
     */
    @ExceptionHandler
    fun handleException(ex: Exception): ResponseEntity<Any> {
        val error = NimError(HttpStatus.BAD_REQUEST, ex.message ?: "Internal error")
        return buildResponseEntity(error)
    }

    /**
     * Creates a [ResponseEntity] for the given [NimError].
     */
    private fun buildResponseEntity(nimError: NimError): ResponseEntity<Any> {
        return ResponseEntity(nimError, nimError.status)
    }
}