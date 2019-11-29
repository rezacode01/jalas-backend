package ir.seven.jalas.services

import ir.seven.jalas.DTO.UserInfo

interface UserService {
    fun getUserInfoById(userId: String): UserInfo
}