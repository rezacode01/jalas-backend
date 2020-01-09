package ir.seven.jalas.services

interface ParticipationService {
    fun addParticipantToMeeting(username: String, meetingId: String)
}