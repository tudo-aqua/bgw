package tools.aqua.bgw.net.server.entity

import org.springframework.data.repository.CrudRepository

interface KeyValueRepository : CrudRepository<KeyValueStoreEntry, String>