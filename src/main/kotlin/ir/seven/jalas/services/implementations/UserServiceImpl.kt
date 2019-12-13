package ir.seven.jalas.services.implementations

import ir.seven.jalas.DTO.UserInfo
import ir.seven.jalas.entities.User
import ir.seven.jalas.enums.ErrorMessage
import ir.seven.jalas.exceptions.EntityDoesNotExist
import ir.seven.jalas.repositories.UserRepo
import ir.seven.jalas.services.UserService
import net.bytebuddy.utility.RandomString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl: UserService {

    @Autowired
    private lateinit var userRepo: UserRepo

    override fun getUserInfoById(userId: String): UserInfo {
        val user = getUserById(userId)
        return UserInfo(user)
    }

    override fun getUserOBjectById(userId: String): User {
        return getUserById(userId)
    }

    override fun getOrCreateUser(username: String): User {
        val user = userRepo.findByUsername(username)
        if (user.isPresent)
            return user.get()
        else {
            val newUser = User(
                    userId = RandomString.make(6),
                    username = username
            )

            val savedObject = userRepo.save(newUser)
            return savedObject
        }
    }

    private fun getUserById(userId: String): User {
        val user = userRepo.findById(userId)
        if (user.isPresent)
            return user.get()
        throw EntityDoesNotExist(ErrorMessage.USER_DOES_NOT_EXIST)
    }
}