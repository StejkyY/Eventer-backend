package eventer.project.app.services

import com.google.inject.Inject
import eventer.project.app.errorhandler.NotFoundException
import eventer.project.app.errorhandler.SomethingWentWrongException
import eventer.project.app.models.objects.Type
import eventer.project.app.repositories.SessionRepository

class TypeService {

    var sessionRepository = SessionRepository()

    suspend fun getTypesList(userId: Int): List<Type> {
        val allTypesList: MutableList<Type> = sessionRepository.getUserTypesList(userId).toMutableList()
        allTypesList.addAll(sessionRepository.getDefaultTypesList())
        return allTypesList
    }

    suspend fun addType(userId: Int, type: Type): Type {
        var typeAdded = sessionRepository.addType(userId, type)
        typeAdded ?: throw SomethingWentWrongException("Error when saving type.")
        return typeAdded
    }

    suspend fun deleteType(id: Int): Boolean {
        val result = sessionRepository.deleteTypeById(id)
        if(!result) throw SomethingWentWrongException("Error when deleting type.")
        return result
    }
}