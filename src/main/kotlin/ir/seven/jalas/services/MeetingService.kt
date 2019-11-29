package ir.seven.jalas.services

import ir.seven.jalas.DTO.MeetingInfo

interface MeetingService {
    fun getMeetingById(meetingId: String): MeetingInfo
    fun chooseSlot(meetingId: String, slotId: String): MeetingInfo
    fun chooseRoom(meetingId: String, roomId: Int): MeetingInfo
}