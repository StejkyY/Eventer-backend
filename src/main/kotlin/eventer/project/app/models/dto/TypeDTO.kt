package eventer.project.app.models.dto

import eventer.project.app.errorhandler.NotFoundException
import eventer.project.app.models.objects.Type
import kotlinx.serialization.Serializable
import org.valiktor.functions.isNotBlank

@Serializable
data class TypeDTO(val type: Type?) {
    fun validate(): Type {
        if(type != null) {
            return org.valiktor.validate(type) {
                validate(Type::name).isNotBlank()
            }
        } else throw NotFoundException("Type not received.")
    }
}

@Serializable
data class TypesDTO(val types: List<Type>)
