package ir.seven.jalas.services

interface AuthorizationService {
    fun isMeetingCreator(username: String, meetingId: String): Boolean
    fun hasParticipatedInMeeting(username: String, meetingId: String): Boolean
    fun isCommentCreator(username: String, commentId: String): Boolean
}