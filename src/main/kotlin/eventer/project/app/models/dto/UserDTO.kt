package eventer.project.app.models.dto

import eventer.project.app.errorhandler.MissingRequestBodyException
import eventer.project.app.errorhandler.NotFoundException
import eventer.project.app.models.objects.User
import eventer.project.app.models.objects.UserCredentials
import eventer.project.app.models.objects.UserPasswordChange
import kotlinx.serialization.Serializable
import org.valiktor.functions.hasSize
import org.valiktor.functions.isEmail
import org.valiktor.functions.isNotBlank

@Serializable
data class UserDTO(val user: User? = null) {
    fun validate(): User {
        if(user != null) {
            return org.valiktor.validate(user) {
                validate(User::firstName).isNotBlank().hasSize(1, 50)
                validate(User::lastName).isNotBlank().hasSize(1, 50)
                validate(User::email).isNotBlank().isEmail()
            }
        } else throw MissingRequestBodyException("User is invalid.")
    }

    fun validateLogin(): User {
        if(user != null) {
            return org.valiktor.validate(user) {
                validate(User::email).isNotBlank()
                validate(User::password).isNotBlank()
            }
        } else throw MissingRequestBodyException("User is invalid.")
    }
}

@Serializable
data class UserPasswordDTO(val userPasswordChange: UserPasswordChange? = null) {
    fun validate(): UserPasswordChange {
        if(userPasswordChange != null) {
            return org.valiktor.validate(userPasswordChange) {
                validate(UserPasswordChange::currentPassword).isNotBlank()
                validate(UserPasswordChange::newPassword).isNotBlank()
            }
        } else throw NotFoundException("User passwords not received.")
    }
}

@Serializable
data class UserCredentialsDTO(val userCredentials: UserCredentials?)

