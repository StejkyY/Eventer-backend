package eventer.project.app.dao

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.ReferenceOption

object UserDao : Table("users") {
    val id = integer("id_user").autoIncrement()
    val firstName = varchar("first_name", 50)
    val lastName = varchar("last_name", 50)
    val email = varchar("email", 50)
    val password = varchar("password", 64)
    val userIdentityId = reference("id_user_identity", UserIdentityDao.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(id)
}