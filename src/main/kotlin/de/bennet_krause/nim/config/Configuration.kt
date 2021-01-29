package de.bennet_krause.nim.config

import de.bennet_krause.nim.game.NimGame
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Configuration {

    /**
     * Bean definition for the NimGame. Allows autowiring without having to add Spring dependencies into the game business logic module.
     */
    @Bean fun nimGame() = NimGame()
}