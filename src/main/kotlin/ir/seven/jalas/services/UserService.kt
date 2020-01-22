package ir.seven.jalas.services

import ir.seven.jalas.dto.MeetingInfo
import ir.seven.jalas.dto.UserInfo
import ir.seven.jalas.entities.User

interface UserService {
    fun getAllMyMeeting(username: String): List<MeetingInfo>

    fun getUserInfoByUsername(username: String): UserInfo
    fun getUserObjectById(userId: String): User
    fun getOrCreateUser(username: String): User
    fun getUserObjectByUsername(username: String): User
}