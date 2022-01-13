package tools.aqua.bgw.net.server.entity

import org.springframework.data.jpa.repository.JpaRepository

interface JsonSchemaRepository : JpaRepository<JsonSchema, String>