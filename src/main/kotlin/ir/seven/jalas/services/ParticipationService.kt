package ir.seven.jalas.services

import ir.seven.jalas.entities.Meeting

interface ParticipationService {
    fun addParticipantsToMeeting(meeting: Meeting, participants: List<String>)
    fun addParticipantToMeeting(username: String, meetingId: String)
    fun removeParticipantFromMeeting(username: String, meetingId: String)

    fun notifyMeetingIsClosed(meetingId: String)
}