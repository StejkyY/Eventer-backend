package eventer.project

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import eventer.project.app.dao.*
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object Db {
    fun init(config: ApplicationConfig) {
        Database.connect(hikari(config))
        transaction {
            SchemaUtils.create(UserDao)
            SchemaUtils.create(UserIdentityDao)
            SchemaUtils.create(EventMemberDao)
            SchemaUtils.create(EventRoleDao)
            SchemaUtils.create(EventDao)
            SchemaUtils.create(SessionDao)
            SchemaUtils.create(TypeDao)
            SchemaUtils.create(LocationDao)
            SchemaUtils.create(GroupDao)
            SchemaUtils.create(GroupMemberDao)
            SchemaUtils.create(GroupRoleDao)
            SchemaUtils.create(CustomScheduleDao)
            SchemaUtils.create(SessionToCustomScheduleDao)
//            initializeDefaultSessionTypes()
//            initializeDefaultEventRoles()
//            initializeDefaultGroupRoles()
        }
    }

    private fun initializeDefaultSessionTypes() {
        TypeDao.insert {
            it[name] = "Break"
        }
        TypeDao.insert {
            it[name] = "Lecture"
        }
        TypeDao.insert {
            it[name] = "Session"
        }
        TypeDao.insert {
            it[name] = "Workshop"
        }
    }

    private fun initializeDefaultEventRoles() {
        EventRoleDao.insert {
            it[name] = "Owner"
        }
        EventRoleDao.insert {
            it[name] = "Attendee"
        }
    }

    private fun initializeDefaultGroupRoles() {
        GroupRoleDao.insert {
            it[name] = "Owner"
        }
        GroupRoleDao.insert {
            it[name] = "Administrator"
        }
        GroupRoleDao.insert {
            it[name] = "Member"
        }
    }

    private fun hikari(config: ApplicationConfig): HikariDataSource {
        val hikariConfig = HikariConfig()
        println(config.keys())
        hikariConfig.driverClassName = config.propertyOrNull("db.driver")?.getString()
        hikariConfig.jdbcUrl = config.propertyOrNull("db.jdbcUrl")?.getString()
        hikariConfig.username = config.propertyOrNull("db.username")?.getString()
        hikariConfig.password = config.propertyOrNull("db.password")?.getString()
        hikariConfig.maximumPoolSize = 3
        hikariConfig.isAutoCommit = false
        hikariConfig.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        hikariConfig.validate()
        return HikariDataSource(hikariConfig)
    }

    suspend fun <T> dbQuery(block: Transaction.() -> T): T = withContext(Dispatchers.IO) {
        transaction {
            block()
        }
    }
}