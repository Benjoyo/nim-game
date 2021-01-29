package de.bennet_krause.nim.controller

import com.fasterxml.jackson.databind.ObjectMapper
import de.bennet_krause.nim.exception.NimError
import de.bennet_krause.nim.game.Status
import de.bennet_krause.nim.game.exception.IllegalMoveException
import de.bennet_krause.nim.model.NimTurn
import de.bennet_krause.nim.model.NimState
import de.bennet_krause.nim.service.NimService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@ExtendWith(SpringExtension::class)
@WebMvcTest
internal class NimControllerTest {

    @MockBean
    private lateinit var nimService: NimService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper


    @Test
    fun `getState should return OK`() {
        mockMvc.perform(get("/nim/state"))
                .andExpect(status().isOk)
    }

    @Test
    fun `postReset should return OK`() {
        mockMvc.perform(post("/nim/reset"))
                .andExpect(status().isOk)
    }

    @Test
    fun `postMove should return OK for valid input`() {
        val move = NimTurn(3)

        mockMvc.perform(post("/nim/turn")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(move)))
                .andExpect(status().isOk)
    }

    @Test
    fun `postMove should return 400 for invalid input`() {
        val move = NimTurn(-1)

        mockMvc.perform(post("/nim/turn")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(move)))
                .andExpect(status().isBadRequest)
    }

    @Test
    fun `postMove should return 400 for missing nimCount`() {
        val move = NimTurn(null)

        mockMvc.perform(post("/nim/turn")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(move)))
                .andExpect(status().isBadRequest)
    }

    @Test
    fun `postMove should call takePlayerTurn`() {
        val move = NimTurn(3)

        mockMvc.perform(post("/nim/turn")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(move)))
                .andExpect(status().isOk)

        verify(nimService).takePlayerTurn(eq(3))
        verifyNoMoreInteractions(nimService)
    }

    @Test
    fun `postReset should call resetGame`() {
        mockMvc.perform(post("/nim/reset"))
                .andExpect(status().isOk)

        verify(nimService).resetGame()
        verifyNoMoreInteractions(nimService)
    }

    @Test
    fun `getState should call currentState`() {
        mockMvc.perform(get("/nim/state"))
                .andExpect(status().isOk)

        verify(nimService).currentState()
        verifyNoMoreInteractions(nimService)
    }

    @Test
    fun `getState should return state`() {
        val state = NimState(13, Status.ONGOING, 0)

        `when`(nimService.currentState()).thenReturn(state)

        mockMvc.perform(get("/nim/state"))
                .andExpect(status().isOk)
                .andExpect(containsObjectAsJson(state, NimState::class.java, objectMapper))
    }

    @Test
    fun `postReset should return state`() {
        val state = NimState(13, Status.ONGOING, 0)

        `when`(nimService.resetGame()).thenReturn(state)

        mockMvc.perform(post("/nim/reset"))
                .andExpect(status().isOk)
                .andExpect(containsObjectAsJson(state, NimState::class.java, objectMapper))
    }

    @Test
    fun `postMove should return state`() {
        val move = NimTurn(3)
        val state = NimState(13, Status.ONGOING, 0)

        `when`(nimService.takePlayerTurn(anyInt())).thenReturn(state)

        mockMvc.perform(post("/nim/turn")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(move)))
                .andExpect(status().isOk)
                .andExpect(containsObjectAsJson(state, NimState::class.java, objectMapper))
    }

    @Test
    fun `postMove with invalid input should return error`() {
        val move = NimTurn(5)

        `when`(nimService.takePlayerTurn(anyInt())).thenThrow(IllegalMoveException(""))

        mockMvc.perform(post("/nim/turn")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(move)))
                .andExpect(status().isBadRequest)
                .andExpect(containsClassAsJson(NimError::class.java, objectMapper))
    }
}

/*
 * Helpers
 */

fun <T> containsObjectAsJson(expectedObject: Any, targetClass: Class<T>, objectMapper: ObjectMapper): ResultMatcher {
    return ResultMatcher { mvcResult: MvcResult ->
        val json = mvcResult.response.contentAsString
        val actualObject = objectMapper.readValue(json, targetClass)
        assertThat(actualObject).usingRecursiveComparison().isEqualTo(expectedObject)
    }
}

fun <T> containsClassAsJson(targetClass: Class<T>, objectMapper: ObjectMapper): ResultMatcher {
    return ResultMatcher { mvcResult: MvcResult ->
        val json = mvcResult.response.contentAsString
        assertDoesNotThrow {
            objectMapper.readValue(json, targetClass)
        }
    }
}