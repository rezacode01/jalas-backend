package ir.seven.jalas.services

import ir.seven.jalas.DTO.UserInfo
import ir.seven.jalas.entities.User

interface UserService {
    fun getUserInfoById(userId: String): UserInfo
    fun getUserOBjectById(userId: String): User
    fun getOrCreateUser(username: String): User
    fun getUserObjectByUsername(username: String): User
}