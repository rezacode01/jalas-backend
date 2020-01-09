package ir.seven.jalas.services

import ir.seven.jalas.entities.Meeting

interface EmailService {
    fun sendMeetingReservedRoomEmail(meetingId: String)
    fun sendMeetingInvitationEmail(meeting: Meeting, email: String)
    fun sendAddSlotEmail(meetingTitle: String, email: String)
    fun sendDeleteSlotEmail(meetingTitle: String, email: String)
    fun sendNewVoteEmail(meetingTitle: String, username: String, email: String)
}