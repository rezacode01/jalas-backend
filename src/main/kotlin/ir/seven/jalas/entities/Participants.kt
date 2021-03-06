package ir.seven.jalas.entities

import ir.seven.jalas.enums.MeetingParticipationRole
import net.bytebuddy.utility.RandomString
import java.util.*
import javax.persistence.*

@Entity
@Table(
        name = "participants",
        uniqueConstraints = [UniqueConstraint(name = "multiple_participants", columnNames = ["user_id", "meeting_id"])]
)
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
        var meeting: Meeting = Meeting(),

        @Column(
                name = "role",
                nullable = false
        )
        var role: MeetingParticipationRole = MeetingParticipationRole.INVITED,

        @Column(
                name = "creation_time",
                nullable = false
        )
        var date: Date = Date()
) {
        @Transient
        fun isCreator(): Boolean {
                if (role == MeetingParticipationRole.CREATOR) return true
                return false
        }
}