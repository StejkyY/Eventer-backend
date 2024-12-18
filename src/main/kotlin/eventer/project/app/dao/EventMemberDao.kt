package eventer.project.app.dao

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object EventMemberDao : Table("event_members") {
    val id = integer("id_event_member").autoIncrement()
    val userId = reference("id_user", UserDao.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val eventId = reference("id_event", EventDao.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val eventRoleId = reference("id_event_role", EventRoleDao.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(id)
}