package eventer.project.app.dao

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.ReferenceOption

object SessionToCustomScheduleDao : Table("session_to_custom_schedule") {
    val sessionId = reference("id_session", SessionDao.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val customScheduleId = reference("id_custom_schedule", CustomScheduleDao.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(arrayOf(sessionId, customScheduleId))
}