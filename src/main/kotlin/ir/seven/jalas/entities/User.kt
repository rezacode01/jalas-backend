package ir.seven.jalas.entities

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
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
    val password: String = ""

)