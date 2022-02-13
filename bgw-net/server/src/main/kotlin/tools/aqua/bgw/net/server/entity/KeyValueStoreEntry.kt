package tools.aqua.bgw.net.server.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "GENERIC_KEY_VALUE_STORE")
class KeyValueStoreEntry(
	@Id
	@Column(name = "key_for_entry", nullable = false, updatable = false)
	val key: String,

	@Column(name = "value_for_entry", nullable = false)
	var value: String,
)