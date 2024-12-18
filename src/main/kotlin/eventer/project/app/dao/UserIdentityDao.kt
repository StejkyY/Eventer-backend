package eventer.project.app.dao

import org.jetbrains.exposed.sql.Table

object UserIdentityDao : Table("user_identities") {
    val id = integer("id_user_identity").autoIncrement()
    val provider = varchar("provider", 20)
    val providerId = varchar("provider_id", 256).nullable()

    override val primaryKey = PrimaryKey(id)
}