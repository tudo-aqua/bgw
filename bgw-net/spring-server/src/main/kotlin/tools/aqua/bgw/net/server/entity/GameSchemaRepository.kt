package tools.aqua.bgw.net.server.entity

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GameSchemaRepository: CrudRepository<GameSchema, String>