package tools.aqua.bgw.net.server.entity

import com.vladmihalcea.hibernate.type.json.JsonType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "JSON_Schema")
@Table(name = "json_schema")
@TypeDef(name = "json", typeClass = JsonType::class)
data class JsonSchema(
	@Id
	@Column(
		name = "id",
		updatable = false,
	)
	val gameID: String,

	@Type(type = "json")
	@Column(
		name = "value",
		updatable = false,
		nullable = false,
		columnDefinition = "jsonb"
	)
	val schema: String,
)