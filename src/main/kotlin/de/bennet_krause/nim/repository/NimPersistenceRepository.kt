package de.bennet_krause.nim.repository

import de.bennet_krause.nim.model.NimPersistence
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NimPersistenceRepository: JpaRepository<NimPersistence, Int>