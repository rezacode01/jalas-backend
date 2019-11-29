package ir.seven.jalas.services.implementations

import ir.seven.jalas.DTO.MeetingInfo
import ir.seven.jalas.entities.Meeting
import ir.seven.jalas.enums.ErrorMessage
import ir.seven.jalas.enums.MeetingStatus
import ir.seven.jalas.exceptions.EntityDoesNotExist
import ir.seven.jalas.repositories.MeetingRepo
import ir.seven.jalas.services.MeetingService
import ir.seven.jalas.services.SlotService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MeetingServiceImpl : MeetingService {

    @Autowired
    private lateinit var meetingRepo: MeetingRepo

    @Autowired
    private lateinit var slotService: SlotService

    override fun getMeetingById(meetingId: String): MeetingInfo {
        val meeting = getMeetingByIdAndHandleException(meetingId)
        return MeetingInfo(meeting)
    }

    override fun chooseSlot(meetingId: String, slotId: String): MeetingInfo {
        val meeting = getMeetingByIdAndHandleException(meetingId)
        val slot = slotService.getSlotObjectById(slotId)

        meeting.slotId = slot
        meeting.state = MeetingStatus.TIME_SUBMITTED

        val savedMeeting = meetingRepo.save(meeting)
        return MeetingInfo(savedMeeting)
    }

    private fun getMeetingByIdAndHandleException(meetingId: String): Meeting {
        val meeting = meetingRepo.findById(meetingId)
        if (meeting.isPresent)
            return meeting.get()
        throw EntityDoesNotExist(ErrorMessage.MEETING_DOES_NOT_EXIST)
    }

}