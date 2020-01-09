package ir.seven.jalas.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "user_notification_management")
class UserNotificationSetting(
        /**
         * user username
         */
        @Id
        @JsonIgnore
        @Column(
                name = "id",
                nullable = false
        )
        var id: String = "",

        @Column(
                name = "meeting_room_reservation",
                nullable = false
        )
        var meetingRoomReservation: Boolean = true,

        @Column(
                name = "adding_slot",
                nullable = false
        )
        var addingSlot: Boolean = true,

        @Column(
                name = "removing_slot",
                nullable = false
        )
        var removingSlot: Boolean = true,

        @Column(
                name = "adding_participant",
                nullable = false
        )
        var addingParticipant: Boolean = true,

        @Column(
                name = "removing_participant",
                nullable = false
        )
        var removingParticipant: Boolean = true,

        @Column(
                name = "vote",
                nullable = false
        )
        var voting: Boolean = true
)