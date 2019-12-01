package ir.seven.jalas.services

import ir.seven.jalas.DTO.AvailableRooms
import ir.seven.jalas.DTO.MeetingInfo
import ir.seven.jalas.enums.MeetingStatus

interface MeetingService {
    fun getMeetingById(meetingId: String): MeetingInfo
    fun getAllMeetings(): List<MeetingInfo>
    fun chooseSlot(meetingId: String, slotId: String): MeetingInfo
    fun getAvailableRooms(meetingId: String): AvailableRooms
    fun chooseRoom(meetingId: String, roomId: Int): MeetingInfo
    fun changeMeetingStats(meetingId: String, status: MeetingStatus): MeetingInfo
    fun getTotalReservedRoomsCount(): Int
}