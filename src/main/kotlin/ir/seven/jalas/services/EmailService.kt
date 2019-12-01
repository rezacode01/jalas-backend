package ir.seven.jalas.services

interface EmailService {
    fun sendMeetingReservedRoomEmail(meetingId: String)
    fun sendSimpleMessage(to: String, subject: String, text: String)
}