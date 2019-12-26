package ir.seven.jalas.entities

import ir.seven.jalas.enums.MeetingStatus
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
        var mid: String = "",

        @Column(
                name = "title",
                nullable = false
        )
        var title: String = "",

        @OneToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "creator")
        var creator: User = User(),

        @Column(
                name = "room_id",
                nullable = true
        )
        var roomId: Int? = null,

        @OneToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "slot_id")
        var slotId: Slot? = null,

        @OneToMany(mappedBy = "meeting", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
        var slots: MutableList<Slot> = mutableListOf(),

        @Column(
                name = "state",
                nullable = false
        )
        var state: MeetingStatus = MeetingStatus.PENDING,

        @OneToMany(
                mappedBy = "meeting",
                fetch = FetchType.LAZY,
                cascade = [CascadeType.ALL]
        )
        var participants: MutableList<Participants> = mutableListOf(),

        @OneToMany(
                mappedBy = "meeting",
                fetch = FetchType.LAZY,
                cascade = [CascadeType.ALL]
        )
        var comments: MutableList<Comment> = mutableListOf()
)