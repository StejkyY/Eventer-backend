package eventer.project.app.dao

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.kotlin.datetime.time

object SessionDao : Table("sessions") {
    val id = integer("id_session").autoIncrement()
    val name = varchar("name", 100)
    val date = date("date")
    val startTime = time("start_time")
    val duration = integer("duration")
    val description = varchar("description", 500).nullable()
    val dayOrder = integer("day_order")
    val typeId = reference("id_type", TypeDao.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val locationId = reference("id_location", LocationDao.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val eventId = reference("id_event", EventDao.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(id)
}