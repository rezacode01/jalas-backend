package ir.seven.jalas.entities

import ir.seven.jalas.enums.ErrorMessage
import ir.seven.jalas.enums.MeetingStatus
import ir.seven.jalas.exceptions.EntityDoesNotExist
import net.bytebuddy.utility.RandomString
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "meetings")
class Meeting (

        @Id
        @Column(
                name = "mid",
                unique = true,
                nullable = false
        )
        var mid: String = RandomString.make(10),

        @Column(
                name = "title",
                nullable = false
        )
        var title: String = "",

        @Column(
                name = "room_id",
                nullable = true
        )
        var roomId: Int? = null,

        @OneToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "slot_id")
        var slotId: Slot? = null,

        @OneToMany(mappedBy = "meeting", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
        var slots: MutableList<Slot> = mutableListOf(),

        @Column(
                name = "state",
                nullable = false
        )
        var state: MeetingStatus = MeetingStatus.POLL,

        @Column(
                name = "changed",
                nullable = false
        )
        var changed: Boolean = false,

        @Column(
                name = "creation_time",
                nullable = false
        )
        var creationTime: Date = Date(),

        @Column(
                name = "submit_time",
                nullable = true
        )
        var submitTime: Date? = null,

        @Column(
                name = "deadline_date",
                nullable = true
        )
        var deadlineDate: Date? = null,

        @OneToMany(
                mappedBy = "meeting",
                fetch = FetchType.LAZY,
                cascade = [CascadeType.ALL]
        )
        var participants: MutableList<Participants> = mutableListOf(),

        @OneToMany(
                mappedBy = "meeting",
                fetch = FetchType.LAZY,
                cascade = [CascadeType.ALL],
                orphanRemoval = true
        )
        var comments: MutableList<Comment> = mutableListOf()
) {
        @Transient
        fun getMeetingCreator(): User {
                val participant = this.participants.find { it.isCreator() } ?:
                        throw EntityDoesNotExist(ErrorMessage.USER_DOES_NOT_EXIST)

                return participant.user
        }

        @Transient
        fun isParticipated(username: String): Boolean {
                this.participants.find { it.user.username == username } ?: return false
                return true
        }

        @Transient
        fun getMeetingCreationTime(): Long {
                return (this.submitTime?.time ?: 0L) - this.creationTime.time
        }
}