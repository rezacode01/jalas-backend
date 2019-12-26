package ir.seven.jalas.services

import ir.seven.jalas.entities.Meeting

interface EmailService {
    fun sendMeetingReservedRoomEmail(meetingId: String)
    fun sendSimpleMessage(to: String, subject: String, text: String)
    fun sendMeetingInvitationEmail(meeting: Meeting, email: String)
    fun sendAddSlotEmail(meetingTitle: String, email: String)
}