package ir.seven.jalas.services

interface ParticipationService {
    fun addParticipantToMeeting(username: String, meetingId: String)
    fun removeParticipantFromMeeting(username: String, meetingId: String)
}