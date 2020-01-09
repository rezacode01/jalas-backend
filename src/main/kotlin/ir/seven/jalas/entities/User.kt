package ir.seven.jalas.entities

import ir.seven.jalas.enums.UserRole
import javax.persistence.*
import javax.validation.constraints.Email

@Entity
@Table(name = "j_user")
class User (

    @Id
    @Column(
            unique = true,
            name = "user_id",
            nullable = false
    )
    var userId: String = "",

    @Column(
            name = "fullname",
            nullable = false
    )
    var fullName: String = "",

    @Email
    @Column(
            name = "username",
            nullable = false,
            unique = true
    )
    var username: String = "",

    @Column(
            name = "password",
            nullable = false
    )
    val password: String = "",

    @Column(
            name = "role",
            nullable = false
    )
    val role: UserRole = UserRole.USER,

    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = [CascadeType.ALL]
    )
    var choices: MutableList<UserChoice> = mutableListOf(),

    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = [CascadeType.ALL]
    )
    var participants: MutableList<Participants> = mutableListOf(),

    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = [CascadeType.ALL]
    )
    var comments: MutableList<Comment> = mutableListOf()
) {
    @Transient
    fun getEmail(): String = this.username
}