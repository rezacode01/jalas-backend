package ir.seven.jalas.services

import ir.seven.jalas.DTO.AvailableRooms
import ir.seven.jalas.DTO.MeetingInfo

interface MeetingService {
    fun getMeetingById(meetingId: String): MeetingInfo
    fun getAllMeetings(): List<MeetingInfo>
    fun chooseSlot(meetingId: String, slotId: String): MeetingInfo
    fun getAvailableRooms(meetingId: String): AvailableRooms
    fun chooseRoom(meetingId: String, roomId: Int): MeetingInfo
}