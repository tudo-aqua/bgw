package tools.aqua.bgw.net.server.entity

import com.vladmihalcea.hibernate.type.json.JsonType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import javax.persistence.*

@Entity(name = "Game")
@TypeDef(name = "json", typeClass = JsonType::class)
data class GameSchema(
	@Id
	@Column(nullable = false, updatable = false)
	val gameID: String,

	@Type(type = "json")
	@Column(
		nullable = false,
		columnDefinition = "jsonb"
	)
	val initActionSchema: String,

	@Type(type = "json")
	@Column(
		nullable = false,
		columnDefinition = "jsonb"
	)
	val gameActionSchema: String,

	@Type(type = "json")
	@Column(
		nullable = false,
		columnDefinition = "jsonb"
	)
	val endActionSchema: String,
)