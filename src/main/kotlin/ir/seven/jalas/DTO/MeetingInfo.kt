package ir.seven.jalas.DTO

import ir.seven.jalas.entities.Meeting
import ir.seven.jalas.enums.MeetingStatus

data class MeetingInfo(
        val id: String,
        val title: String,
        val creator: UserInfo,
        val slots: List<SlotInfo>,
        val selectedSlot: SlotInfo?,
        val room: Int?,
        val state: MeetingStatus
) {
    constructor(meeting: Meeting) : this(
            meeting.mid,
            meeting.title,
            UserInfo(meeting.getMeetingCreator()),
            meeting.slots.map { SlotInfo.toSlotInfo(it) },
            if (meeting.slotId != null) SlotInfo.toSlotInfo(meeting.slotId!!) else null,
            meeting.roomId,
            meeting.state

    )
}