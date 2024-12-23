package eventer.project.app.dao

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table


object CustomScheduleDao : Table("custom_schedules") {
    val id = integer("id_custom_schedule").autoIncrement()
    val name = varchar("name", 100)
    val eventId = reference("id_event", EventDao.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(id)
}