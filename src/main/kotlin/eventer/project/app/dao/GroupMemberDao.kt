package eventer.project.app.dao

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object GroupMemberDao : Table("group_members") {
    val id = integer("id_group_member").autoIncrement()
    val userId = reference("id_user", UserDao.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val groupId = reference("id_group", GroupDao.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val groupRoleId = reference("id_group_role", GroupRoleDao.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(id)
}