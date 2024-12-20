package eventer.project.app.services

import com.google.inject.Inject
import eventer.project.app.errorhandler.AuthorizationException
import eventer.project.app.errorhandler.NotFoundException
import eventer.project.app.errorhandler.SomethingWentWrongException
import eventer.project.app.models.objects.User
import eventer.project.app.models.objects.UserPasswordChange
import eventer.project.app.repositories.UserRepository
import eventer.project.utils.JwtProvider
import org.apache.commons.codec.digest.DigestUtils

class UserService {

    private val jwtProvider = JwtProvider

    var userRepository = UserRepository()

    suspend fun getUserByEmail(email: String): User {
        val userFound = userRepository.getUserByEmail(email)
        return userFound ?: throw NotFoundException("User not found.")
    }

    suspend fun authenticate(user: User): User {
        val userFound = userRepository.getUserByEmail(user.email!!)
        if (userFound?.password == DigestUtils.sha256Hex(user.password)) {
            return userFound!!.copy(token = generateJwtToken(userFound))
        }
        throw AuthorizationException("Email or password invalid.")
    }

    suspend fun addUser(user: User): User {
        val userFound = userRepository.getUserByEmail(user.email!!)
        if (userFound != null) {
            throw SomethingWentWrongException("User with this email already exists.")
        }
        var userAdded = userRepository.addUser(user)
        return userAdded ?: throw SomethingWentWrongException("Error when saving user.")
    }

    suspend fun updateUser(user: User): User {
        val receivedUser = userRepository.updateUser(user)
        receivedUser ?: throw SomethingWentWrongException("Error when updating user.")
        return receivedUser.copy(token = generateJwtToken(receivedUser))
    }

    suspend fun deleteUserById(id: Int): Boolean {
        val result = userRepository.deleteUserById(id)
        if(!result) throw SomethingWentWrongException("Error when deleting user.")
        return result
    }

    suspend fun changeUserPasswordById(id: Int, passwordChange: UserPasswordChange): Boolean {
        val result = userRepository.changeUserPasswordById(id, passwordChange)
        if(!result) throw SomethingWentWrongException("Current password is not correct.")
        return result
    }

    private fun generateJwtToken(user: User): String? {
        return jwtProvider.createJWT(user)
    }
}