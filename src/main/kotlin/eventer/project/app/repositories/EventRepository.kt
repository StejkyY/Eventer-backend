package eventer.project.app.repositories

import eventer.project.Db
import eventer.project.app.errorhandler.NotFoundException
import eventer.project.app.models.objects.*
import eventer.project.app.dao.EventDao
import eventer.project.app.dao.EventMemberDao
import eventer.project.app.dao.EventRoleDao
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class EventRepository {

    /**
     * Converts a database row into an `Event` object.
     */
    private fun toEvent(row: ResultRow): Event {
        return Event(
            id = row[EventDao.id],
            name = row[EventDao.name],
            location = row[EventDao.location],
            startDate = row[EventDao.startDate],
            startTime = row[EventDao.startTime],
            endDate = row[EventDao.endDate],
            endTime = row[EventDao.endTime],
            description = row[EventDao.description],
            type = EventType.valueOf(row[EventDao.type]),
            state = EventState.valueOf(row[EventDao.state]),
            userEventRole = toEventRole(row),
            privacy = EventPrivacy.valueOf(row[EventDao.privacy])
        )
    }

    /**
     * Converts a database row into a `Event role` object.
     */
    private fun toEventRole(row: ResultRow): EventRole {
        return EventRole(
            id = row[EventRoleDao.id],
            name = row[EventRoleDao.name],
        )
    }

    /**
     * Returns a list of all events stored in the database.
     */
    suspend fun getEventsList(): List<Event> {
        return Db.dbQuery {
            (EventDao innerJoin EventMemberDao innerJoin EventRoleDao).selectAll().mapNotNull { toEvent(it) }
        }
    }

    /**
     * Returns a list of all events stored in the database, which were created by a user, by his ID.
     */
    suspend fun getUserEventsList(userId: Int): List<Event> {
        return Db.dbQuery {
            (EventDao innerJoin EventMemberDao innerJoin EventRoleDao)
                .selectAll().where { EventMemberDao.userId eq userId }
                .map {toEvent(it)}
        }
    }

    /**
     * Returns a list of all event roles stored in the database.
     */
    suspend fun getEventRolesList(): List<EventRole> {
        return Db.dbQuery {
            EventRoleDao.selectAll().mapNotNull { toEventRole(it) }
        }
    }

    /**
     * Returns an event stored in the database by his ID.
     */
    suspend fun getEventById(id: Int): Event? {
        return Db.dbQuery {
            (EventDao innerJoin EventMemberDao innerJoin EventRoleDao).selectAll().where{
                EventDao.id eq id
            }.mapNotNull{toEvent(it)}.singleOrNull()
        }
    }

    /**
     * Returns an event stored in the database by his ID with the role of the user who requested it.
     */
    suspend fun getEventByIdWithRole(userId: Int, eventId: Int): Event? {
        return Db.dbQuery {
            (EventDao innerJoin EventMemberDao innerJoin EventRoleDao)
                .selectAll().where { (EventDao.id eq eventId) and (EventMemberDao.userId eq userId) }
                .mapNotNull {toEvent(it)}.singleOrNull()
        }
    }

    /**
     * Adds an event into the database.
     * Also inserts new EventMember connecting the event and an user
     */
    suspend fun addEvent(userId: Int, event: Event): Event? {
        val key = Db.dbQuery {
            (EventDao.insert {
                it[name] = event.name!!
                it[location] = event.location!!
                it[startDate] = event.startDate!!
                it[endDate] = event.endDate!!
                it[startTime] = event.startTime
                it[endTime] = event.endTime
                it[description] = event.description
                it[type] = event.type.toString()
                it[state] = event.state.toString()
                it[privacy] = event.privacy.toString()
            } get EventDao.id)
        }
        Db.dbQuery {
            EventMemberDao.insert {
                it[this.userId] = userId
                it[eventId] = key
                it[eventRoleId] = event.userEventRole?.id!!
            }
        }
        return getEventByIdWithRole(userId, key)
    }

    /**
     * Returns user role in a specified event.
     */
    suspend fun getUserRoleInEvent(userId: Int, eventId: Int): EventRole? {
        return Db.dbQuery {
            (EventMemberDao innerJoin EventRoleDao)
                .selectAll().where { (EventMemberDao.userId eq userId) and (EventMemberDao.eventId eq eventId) }
                .mapNotNull {toEventRole(it)}.singleOrNull()
        }
    }

    /**
     * Updates event stored in the database.
     */
    suspend fun updateEvent(userId: Int? = null, event: Event): Event? {
        val id = event.id
        id ?: throw NotFoundException("Event not found")
        Db.dbQuery {
            EventDao.update({EventDao.id eq event.id}) {
                it[name] = event.name!!
                it[location] = event.location!!
                it[startDate] = event.startDate!!
                it[endDate] = event.endDate!!
                it[startTime] = event.startTime
                it[endTime] = event.endTime
                it[description] = event.description
                it[type] = event.type.toString()
                it[state] = event.state.toString()
                it[privacy] = event.privacy.toString()
            }
        }
        if(userId == null) return getEventById(id)
        else return getEventByIdWithRole(userId, id)
    }

    /**
     * Deletes event stored in the database by its ID.
     */
    suspend fun deleteEventById(id: Int): Boolean {
        return Db.dbQuery {
            EventDao.deleteWhere { (EventDao.id eq id) } > 0
            EventMemberDao.deleteWhere { eventId eq id } >= 0
        }
    }
}