package eventer.project.app.dao

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object TypeDao : Table("types") {
    val id = integer("id_type").autoIncrement()
    val name = varchar("name", 50)
    val userId = reference("id_user", UserDao.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE).nullable()

    override val primaryKey = PrimaryKey(id)
}