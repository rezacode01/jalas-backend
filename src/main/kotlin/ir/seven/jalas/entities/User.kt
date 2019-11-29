package ir.seven.jalas.entities

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
            nullable = false
    )
    var username: String = "",

    @Column(
            name = "password",
            nullable = false
    )
    val password: String = "",

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    var choices: MutableList<UserChoice> = mutableListOf()

)