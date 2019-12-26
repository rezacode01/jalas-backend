package ir.seven.jalas.entities

import net.bytebuddy.utility.RandomString
import javax.persistence.*

@Entity
@Table(name = "participants")
class Participants(
        @Id
        @Column(
                name = "pid",
                nullable = false,
                unique = true
        )
        var pid: String = RandomString.make(12),

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "user_id")
        var user: User = User(),

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "meeting_id")
        var meeting: Meeting = Meeting()
)