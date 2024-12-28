package eventer.project.app.repositories

import eventer.project.Db
import eventer.project.app.errorhandler.NotFoundException
import eventer.project.app.models.objects.Location
import eventer.project.app.models.objects.Session
import eventer.project.app.models.objects.Type
import eventer.project.app.dao.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class SessionRepository {

    /**
     * Converts a database row into an `Session` object.
     */
    private fun toSession(row: ResultRow): Session {
        val sessionType = runBlocking {getTypeById(row[SessionDao.typeId])}
        val sessionLocation = runBlocking {getLocationById(row[SessionDao.locationId])}

        return Session(
            id = row[SessionDao.id],
            name = row[SessionDao.name],
            date = row[SessionDao.date],
            startTime = row[SessionDao.startTime],
            duration = row[SessionDao.duration],
            description = row[SessionDao.description],
            dayOrder = row[SessionDao.dayOrder],
            type = sessionType,
            location = sessionLocation,
            eventId = row[SessionDao.eventId]
        )
    }

    /**
     * Returns a list of session for an event by its ID from the database.
     */
    suspend fun getSessionListByEventId(id: Int): List<Session> {
        return Db.dbQuery {
            SessionDao.selectAll().where {
                SessionDao.eventId eq id
            }.mapNotNull { toSession(it) }
        }
    }

    /**
     * Returns a session by its ID from the database.
     */
    suspend fun getSessionById(id: Int): Session? {
        return Db.dbQuery {
            SessionDao.selectAll().where{
                SessionDao.id eq id
            }.mapNotNull{toSession(it)}.singleOrNull()
        }
    }

    /**
     * Updates event agenda in the database.
     * Adds new sessions, updates edited sessions and deletes removed sessions.
     */
    suspend fun updateEventSessionAgenda(addedSessions: List<Session>,
                                         updatedSessions: List<Session>,
                                         deletedSessions: List<Session>) {
        Db.dbQuery {
            for (addedSession in addedSessions) {
                SessionDao.insert {
                    it[name] = addedSession.name!!
                    it[date] = addedSession.date!!
                    it[startTime] = addedSession.startTime!!
                    it[duration] = addedSession.duration!!
                    it[description] = addedSession.description
                    it[dayOrder] = addedSession.dayOrder!!
                    it[typeId] = addedSession.type!!.id!!
                    it[locationId] = addedSession.location!!.id!!
                    it[eventId] = addedSession.eventId!!
                }
            }
            for (updatedSession in updatedSessions) {
                SessionDao.update({SessionDao.id eq updatedSession.id!!}) {
                    it[name] = updatedSession.name!!
                    it[date] = updatedSession.date!!
                    it[startTime] = updatedSession.startTime!!
                    it[duration] = updatedSession.duration!!
                    it[description] = updatedSession.description
                    it[dayOrder] = updatedSession.dayOrder!!
                    it[typeId] = updatedSession.type?.id!!
                    it[locationId] = updatedSession.location?.id!!
                    it[eventId] = updatedSession.eventId!!
                }
            }
            for (deletedSession in deletedSessions) {
                SessionDao.deleteWhere { (SessionDao.id eq deletedSession.id!!) } > 0
                LocationDao.deleteWhere { (LocationDao.eventId eq deletedSession.id!!) } > 0
            }
        }
    }

    /**
     * Inserts a session into the database.
     */
    suspend fun addSession(session: Session): Session? {
        val key = Db.dbQuery {
            (SessionDao.insert {
                it[name] = session.name!!
                it[date] = session.date!!
                it[startTime] = session.startTime!!
                it[duration] = session.duration!!
                it[description] = session.description
                it[dayOrder] = session.dayOrder!!
                it[typeId] = session.type!!.id!!
                it[locationId] = session.location!!.id!!
                it[eventId] = session.eventId!!
            } get EventDao.id)
        }
        return getSessionById(key)
    }

    /**
     * Updates a session in the database by its ID
     */
    suspend fun updateSession(session: Session): Session? {
        val id = session.id
        id ?: throw NotFoundException("Session not found")
        Db.dbQuery {
            SessionDao.update({SessionDao.id eq session.id}) {
                it[name] = session.name!!
                it[date] = session.date!!
                it[startTime] = session.startTime!!
                it[duration] = session.duration!!
                it[description] = session.description
                it[dayOrder] = session.dayOrder!!
                it[typeId] = session.type?.id!!
                it[locationId] = session.location?.id!!
                it[eventId] = session.eventId!!
            }
        }
        return getSessionById(id)
    }

    /**
     * Deletes a session by its ID from the database.
     */
    suspend fun deleteSessionById(id: Int): Boolean {
        return Db.dbQuery {
            SessionDao.deleteWhere { (SessionDao.id eq id) } > 0
            LocationDao.deleteWhere { (LocationDao.eventId eq id) } > 0
        }
    }

    /**
     * Converts a database row into a `Type` object.
     */
    private fun toType(row: ResultRow): Type {
        return Type(
            id = row[TypeDao.id],
            name = row[TypeDao.name],
            userId = row[TypeDao.userId]
        )
    }

    /**
     * Returns a list of all custom session types for the given user from the database.
     */
    suspend fun getUserTypesList(userId: Int): List<Type> {
        return Db.dbQuery {
            TypeDao.selectAll().where{
                TypeDao.userId eq userId
            }.mapNotNull { toType(it) }
        }
    }

    /**
     * Returns a list of all default session types from the database.
     */
    suspend fun getDefaultTypesList(): List<Type> {
        return Db.dbQuery {
            TypeDao.selectAll().where{
                TypeDao.userId.isNull()
            }.mapNotNull { toType(it) }
        }
    }

    /**
     * Returns a session type from the database by its ID.
     */
    suspend fun getTypeById(id: Int): Type? {
        return Db.dbQuery {
            TypeDao.selectAll().where{
                TypeDao.id eq id
            }.mapNotNull{toType(it)}.singleOrNull()
        }
    }

    /**
     * Returns a new session type into the database.
     */
    suspend fun addType(userId: Int, type: Type): Type? {
        val key = Db.dbQuery {
            (TypeDao.insert {
                it[name] = type.name!!
                it[TypeDao.userId] = userId
            } get TypeDao.id)
        }
        return getTypeById(key)
    }

    /**
     * Deletes a session type from the database by its ID.
     */
    suspend fun deleteTypeById(id: Int): Boolean {
        return Db.dbQuery {
            TypeDao.deleteWhere { (TypeDao.id eq id) } > 0
        }
    }

    /**
     * Converts a database row into a `Location` object.
     */
    private fun toLocation(row: ResultRow): Location {
        return Location(
            id = row[LocationDao.id],
            name = row[LocationDao.name],
            eventId = row[LocationDao.eventId]
        )
    }

    /**
     * Returns a list of all locations for event sessions by event ID from the database.
     */
    suspend fun getEventLocationsList(eventId: Int): List<Location> {
        return Db.dbQuery {
            LocationDao.selectAll().where {
                LocationDao.eventId eq eventId
            }.mapNotNull { toLocation(it) }
        }
    }

    /**
     * Returns session location by its ID from the database.
     */
    suspend fun getLocationById(id: Int): Location? {
        return Db.dbQuery {
            LocationDao.selectAll().where{
                LocationDao.id eq id
            }.mapNotNull{toLocation(it)}.singleOrNull()
        }
    }

    /**
     * Inserts a new session location into the database.
     */
    suspend fun addLocation(location: Location): Location? {
        val key = Db.dbQuery {
            (LocationDao.insert {
                it[name] = location.name!!
                it[eventId] = location.eventId!!
            } get LocationDao.id)
        }
        return getLocationById(key)
    }

    /**
     * Deletes session location by its ID from the database.
     */
    suspend fun deleteLocationById(id: Int): Boolean {
        return Db.dbQuery {
            LocationDao.deleteWhere { (LocationDao.id eq id) } > 0
        }
    }


}