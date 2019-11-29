package ir.seven.jalas.DTO

import ir.seven.jalas.entities.Meeting

data class MeetingInfo(
        val id: String,
        val title: String,
        val creator: UserInfo,
        val slots: List<SlotInfo>
) {
    constructor(meeting: Meeting) : this(meeting.mid, meeting.title, UserInfo(meeting.creator), meeting.slots.map { SlotInfo(it) })
}