package eventer.project.app.dao

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.kotlin.datetime.time

object EventDao : Table("events") {
    val id = integer("id_event").autoIncrement()
    val name = varchar("name", 100)
    val location = varchar("location", 100)
    val startDate = date("start_date")
    val endDate = date("end_date")
    val startTime = time("start_time").nullable()
    val endTime = time("end_time").nullable()
    val description = text("description").nullable()
    val type = varchar("type", 50)
    val state = varchar("state", 50)
    val privacy = varchar("privacy", 50)

    override val primaryKey = PrimaryKey(id)
}