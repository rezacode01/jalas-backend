package ir.seven.jalas.services

import ir.seven.jalas.enums.MeetingParticipationRole

interface ParticipationService {
    fun addParticipantsToMeeting(meetingId: String, participants: Map<String, MeetingParticipationRole>)
}