package eventer.project.app.dao

import org.jetbrains.exposed.sql.Table

object GroupDao : Table("groups") {
    val id = integer("id_group").autoIncrement()
    val name = varchar("name", 50)
    val internalNote = varchar("internal_note", 300).nullable()

    override val primaryKey = PrimaryKey(id)
}