package ir.seven.jalas.dto

import ir.seven.jalas.entities.User

data class UserInfo (
        val userId: String,
        val fullname: String,
        val username: String
) {
    constructor(user: User) : this(user.userId, user.fullName, user.username)
}