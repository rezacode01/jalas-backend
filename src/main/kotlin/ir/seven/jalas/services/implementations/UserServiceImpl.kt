package ir.seven.jalas.services.implementations

import ir.seven.jalas.dto.MeetingInfo
import ir.seven.jalas.dto.UserInfo
import ir.seven.jalas.entities.User
import ir.seven.jalas.enums.ErrorMessage
import ir.seven.jalas.exceptions.EntityDoesNotExist
import ir.seven.jalas.repositories.UserRepo
import ir.seven.jalas.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserServiceImpl: UserService {

    @Autowired
    private lateinit var userRepo: UserRepo

    override fun getAllMyMeeting(username: String): List<MeetingInfo> {
        val user = getUserObjectByUsername(username)
        val myMeeting: MutableMap<String, MeetingInfo> = mutableMapOf()

        user.participants.forEach {
            myMeeting[it.meeting.mid] = MeetingInfo(it.meeting)
        }

        return myMeeting.values.toList()
    }

    override fun getUserInfoByUsername(username: String): UserInfo {
        val user = getUserObjectByUsername(username)
        return UserInfo(user)
    }

    override fun getUserObjectById(userId: String): User {
        return getUserById(userId)
    }

    override fun getUserObjectByUsername(username: String): User {
        val user = userRepo.findByUsername(username)

        if (user.isPresent)
            return user.get()
        throw EntityDoesNotExist(ErrorMessage.USER_DOES_NOT_EXIST)
    }

    override fun getOrCreateUser(username: String): User {
        val user = userRepo.findByUsername(username)

        return if (user.isPresent)
            user.get()
        else {
            userRepo.save(User(username = username))
        }
    }

    private fun getUserById(userId: String): User {
        val user = userRepo.findById(userId)
        if (user.isPresent)
            return user.get()
        throw EntityDoesNotExist(ErrorMessage.USER_DOES_NOT_EXIST)
    }
}