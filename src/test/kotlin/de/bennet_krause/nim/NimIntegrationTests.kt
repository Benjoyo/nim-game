package de.bennet_krause.nim

import com.fasterxml.jackson.databind.ObjectMapper
import de.bennet_krause.nim.controller.NimController
import de.bennet_krause.nim.controller.containsObjectAsJson
import de.bennet_krause.nim.game.NimGame
import de.bennet_krause.nim.game.Status
import de.bennet_krause.nim.model.NimConfig
import de.bennet_krause.nim.model.NimTurn
import de.bennet_krause.nim.model.NimState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(SpringExtension::class)
@SpringBootTest
class NimIntegrationTests {

    @Autowired
    private lateinit var nimController: NimController

    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper


    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(nimController).build()
    }

    @Test
    fun `test nim game`() {
        // state should return initial state
        val state = NimState(13, Status.ONGOING, 3,0)
        mockMvc.perform(MockMvcRequestBuilders.get("/nim/state"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(containsObjectAsJson(state, NimState::class.java, objectMapper))

        // should execute turn
        val move = NimTurn(3)
        mockMvc.perform(MockMvcRequestBuilders.post("/nim/turn")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(move)))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect {
                    val json = it.response.contentAsString
                    val s = objectMapper.readValue(json, NimState::class.java)

                    assertEquals(s.status, Status.ONGOING)
                    assertTrue(s.lastComputerMove > 0)
                    assertTrue(s.pile < NimGame.DEFAULT_INITIAL_PILE_SIZE)
                }

        // reset should return initial state
        mockMvc.perform(MockMvcRequestBuilders.post("/nim/reset"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(containsObjectAsJson(state, NimState::class.java, objectMapper))

        // state should return initial state again
        mockMvc.perform(MockMvcRequestBuilders.get("/nim/state"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(containsObjectAsJson(state, NimState::class.java, objectMapper))

        // should allow config
        val config = NimConfig(10, 5)
        mockMvc.perform(MockMvcRequestBuilders.post("/nim/config")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(config)))
                .andExpect(MockMvcResultMatchers.status().isOk)

        val newState = NimState(10, Status.ONGOING, 5,0)

        // reset should return new initial state
        mockMvc.perform(MockMvcRequestBuilders.post("/nim/reset"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(containsObjectAsJson(newState, NimState::class.java, objectMapper))

        // state should return new initial state
        mockMvc.perform(MockMvcRequestBuilders.get("/nim/state"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(containsObjectAsJson(newState, NimState::class.java, objectMapper))
    }
}
