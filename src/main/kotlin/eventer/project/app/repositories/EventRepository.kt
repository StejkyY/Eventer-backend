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

    private fun toEventRole(row: ResultRow): EventRole {
        return EventRole(
            id = row[EventRoleDao.id],
            name = row[EventRoleDao.name],
        )
    }

    suspend fun getEventsList(): List<Event> {
        return Db.dbQuery {
            (EventDao innerJoin EventMemberDao innerJoin EventRoleDao).selectAll().mapNotNull { toEvent(it) }
        }
    }

    suspend fun getUserEventsList(userId: Int): List<Event> {
        return Db.dbQuery {
            (EventDao innerJoin EventMemberDao innerJoin EventRoleDao)
                .selectAll().where { EventMemberDao.userId eq userId }
                .map {toEvent(it)}
        }
    }

    suspend fun getEventRolesList(): List<EventRole> {
        return Db.dbQuery {
            EventRoleDao.selectAll().mapNotNull { toEventRole(it) }
        }
    }

    suspend fun getEventById(id: Int): Event? {
        return Db.dbQuery {
            (EventDao innerJoin EventMemberDao innerJoin EventRoleDao).selectAll().where{
                EventDao.id eq id
            }.mapNotNull{toEvent(it)}.singleOrNull()
        }
    }

    suspend fun getEventByIdWithRole(userId: Int, eventId: Int): Event? {
        return Db.dbQuery {
            (EventDao innerJoin EventMemberDao innerJoin EventRoleDao)
                .selectAll().where { (EventDao.id eq eventId) and (EventMemberDao.userId eq userId) }
                .mapNotNull {toEvent(it)}.singleOrNull()
        }
    }

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

    suspend fun getUserRoleInEvent(userId: Int, eventId: Int): EventRole? {
        return Db.dbQuery {
            (EventMemberDao innerJoin EventRoleDao)
                .selectAll().where { (EventMemberDao.userId eq userId) and (EventMemberDao.eventId eq eventId) }
                .mapNotNull {toEventRole(it)}.singleOrNull()
        }
    }

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

    suspend fun deleteEventById(id: Int): Boolean {
        return Db.dbQuery {
            EventDao.deleteWhere { (EventDao.id eq id) } > 0
            EventMemberDao.deleteWhere { eventId eq id } >= 0
        }
    }
}