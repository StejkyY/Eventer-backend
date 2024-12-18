package eventer.project.app.dao

import org.jetbrains.exposed.sql.Table

object TypeDao : Table("types") {
    val id = integer("id_type").autoIncrement()
    val name = varchar("name", 50)

    override val primaryKey = PrimaryKey(id)
}