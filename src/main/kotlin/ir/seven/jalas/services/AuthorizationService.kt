package ir.seven.jalas.services

interface AuthorizationService {
    fun hasParticipatedInMeeting(username: String, meetingId: String): Boolean
}