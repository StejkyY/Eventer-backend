package eventer.project.app.dao

import org.jetbrains.exposed.sql.Table

object GroupRoleDao : Table("group_roles") {
    val id = integer("id_group_role").autoIncrement()
    val name = varchar("name", 50)

    override val primaryKey = PrimaryKey(id)
}