package ir.seven.jalas.services

import ir.seven.jalas.DTO.MeetingInfo

interface MeetingService {
    fun getMeetingById(meetingId: String): MeetingInfo
}