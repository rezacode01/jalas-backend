package ir.seven.jalas.DTO

import ir.seven.jalas.entities.Meeting

class MeetingInfo(
        id: String,
        title: String,
        creator: UserInfo,
        slots: List<SlotInfo>
) {
    constructor(meeting: Meeting) : this(meeting.mid, meeting.title, UserInfo(meeting.creator), meeting.slots.map { SlotInfo(it) })
}