package eventer.project.app.dao

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object LocationDao : Table("locations") {
    val id = integer("id_location").autoIncrement()
    val name = varchar("name", 50)
    val eventId = reference("id_event", EventDao.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(id)
}