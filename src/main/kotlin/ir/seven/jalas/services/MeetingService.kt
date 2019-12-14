package ir.seven.jalas.services

import ir.seven.jalas.DTO.AvailableRooms
import ir.seven.jalas.DTO.CreateMeetingRequest
import ir.seven.jalas.DTO.MeetingInfo
import ir.seven.jalas.DTO.VoteMeetingRequest
import ir.seven.jalas.entities.Meeting
import ir.seven.jalas.enums.MeetingStatus
import ir.seven.jalas.enums.UserChoiceState

interface MeetingService {
    fun createMeeting(userId: String, request: CreateMeetingRequest): MeetingInfo
    fun getMeetingById(meetingId: String): MeetingInfo
    fun getMeetingObjectById(meetingId: String): Meeting
    fun getAllMeetings(): List<MeetingInfo>
    fun chooseSlot(meetingId: String, slotId: String): MeetingInfo
    fun voteSlot(meetingId: String, slotId: String, request: VoteMeetingRequest): MeetingInfo
    fun getAvailableRooms(meetingId: String): AvailableRooms
    fun chooseRoom(meetingId: String, roomId: Int): MeetingInfo
    fun changeMeetingStats(meetingId: String, status: MeetingStatus): MeetingInfo
    fun getTotalReservedRoomsCount(): Int
}