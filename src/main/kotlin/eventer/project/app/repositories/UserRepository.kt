package eventer.project.app.repositories

import eventer.project.Db
import eventer.project.app.errorhandler.NotFoundException
import eventer.project.app.models.objects.IdentityProvider
import eventer.project.app.models.objects.User
import eventer.project.app.models.objects.UserPasswordChange
import eventer.project.app.dao.UserDao
import eventer.project.app.dao.UserIdentityDao
import org.apache.commons.codec.digest.DigestUtils
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserRepository {

    /**
     * Converts a database row into a `User` object.
     */
    private fun toUser(row: ResultRow): User =
        User(
            id = row[UserDao.id],
            firstName = row[UserDao.firstName],
            lastName = row[UserDao.lastName],
            email = row[UserDao.email],
            password = row[UserDao.password],
            userIdentityId = row[UserDao.userIdentityId]
        )

    /**
     * Returns a user by email from the database.
     */
    suspend fun getUserByEmail(email: String): User? {
        return Db.dbQuery {
            UserDao.selectAll().where{
                UserDao.email eq email
            }.mapNotNull{toUser(it)}.singleOrNull()
        }
    }

    /**
     * Returns a user by his ID from the database.
     */
    suspend fun getUserById(id: Int): User? {
        return Db.dbQuery {
            UserDao.selectAll().where{
                UserDao.id eq id
            }.mapNotNull{toUser(it)}.singleOrNull()
        }
    }

    /**
     * Inserts a user into the database.
     * Also inserts local identity provider connected to the user into the database.
     */
    suspend fun addUser(user: User): User? {
        val identityKey = Db.dbQuery {
            (UserIdentityDao.insert {
                it[this.provider] = IdentityProvider.Local.toString()
            } get UserIdentityDao.id)
        }
        val key = Db.dbQuery {
            (UserDao.insert {
                it[this.firstName] = user.firstName!!
                it[this.lastName] = user.lastName!!
                it[this.email] = user.email!!
                it[this.password] = DigestUtils.sha256Hex(user.password!!)
                it[this.userIdentityId] = identityKey
            } get UserDao.id)
        }
        return getUserById(key)
    }

    /**
     * Updates given user in the database.
     */
    suspend fun updateUser(user: User): User? {
        val id = user.id
        id ?: throw NotFoundException("User not found.")
        Db.dbQuery {
            UserDao.update({UserDao.id eq id}) {
                it[this.firstName] = user.firstName!!
                it[this.lastName] = user.lastName!!
                it[this.email] = user.email!!
            }
        }
        return getUserById(id)
    }

    /**
     * Deletes user by his ID from the database.
     */
    suspend fun deleteUserById(id: Int): Boolean {
        val user = getUserById(id)
        return Db.dbQuery {
            UserDao.deleteWhere { (UserDao.id eq id) } > 0
            UserIdentityDao.deleteWhere {UserIdentityDao.id eq user!!.userIdentityId!! } > 0
        }
    }

    /**
     * Changes user password by his ID.
     * Checks that the hash of given current password is same as the one stored in the database.
     */
    suspend fun changeUserPasswordById(id: Int, passwordChange: UserPasswordChange): Boolean {
        val currentUser = getUserById(id)
        if(DigestUtils.sha256Hex(passwordChange.currentPassword) != currentUser?.password) {
            return false
        } else {
            Db.dbQuery {
                (UserDao.update({UserDao.id eq id}) {
                    it[this.password] = DigestUtils.sha256Hex(passwordChange.newPassword)
                })
            }
            return true
        }
    }
}