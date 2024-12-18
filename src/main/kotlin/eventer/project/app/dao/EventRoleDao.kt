package eventer.project.app.dao

import org.jetbrains.exposed.sql.Table

object EventRoleDao : Table("event_roles") {
    val id = integer("id_event_role").autoIncrement()
    val name = varchar("name", 50)

    override val primaryKey = PrimaryKey(id)
}