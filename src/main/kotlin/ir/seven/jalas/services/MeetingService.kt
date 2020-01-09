package ir.seven.jalas.services

import ir.seven.jalas.DTO.*
import ir.seven.jalas.entities.Meeting
import ir.seven.jalas.enums.MeetingStatus
import ir.seven.jalas.enums.UserChoiceState

interface MeetingService {
    fun createMeeting(username: String, request: CreateMeetingRequest): MeetingInfo
    fun addSlot(meetingId: String, request: CreateSlotRequest): MeetingInfo
    fun getMeetingById(meetingId: String): MeetingInfo
    fun getMeetingObjectById(meetingId: String): Meeting
    fun getAllMeetings(): List<MeetingInfo>
    fun chooseSlot(meetingId: String, slotId: String): MeetingInfo
    fun voteSlot(meetingId: String, slotId: String, username: String, vote: UserChoiceState): MeetingInfo
    fun getAvailableRooms(meetingId: String): AvailableRooms
    fun chooseRoom(meetingId: String, roomId: Int): MeetingInfo
    fun changeMeetingState(meetingId: String, status: MeetingStatus): MeetingInfo

    fun getTotalReservedRoomsCount(): Int
    fun getTotalCancelledMeetings(): Int
    fun getTotalChangedMeetings(): Int
    fun getAverageMeetingCreationTime(): Double

    fun isMeetingCreator(username: String, meetingId: String): Boolean
    fun hasParticipatedInMeeting(username: String, meetingId: String): Boolean
}