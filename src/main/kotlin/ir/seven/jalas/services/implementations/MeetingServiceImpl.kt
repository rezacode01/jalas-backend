package ir.seven.jalas.services.implementations

import ir.seven.jalas.DTO.MeetingInfo
import ir.seven.jalas.entities.Meeting
import ir.seven.jalas.enums.ErrorMessage
import ir.seven.jalas.exceptions.EntityDoesNotExist
import ir.seven.jalas.repositories.MeetingRepo
import ir.seven.jalas.services.MeetingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MeetingServiceImpl : MeetingService {

    @Autowired
    private lateinit var meetingRepo: MeetingRepo

    override fun getMeetingById(meetingId: String): MeetingInfo {
        val meeting = getMeetingByIdAndHandleException(meetingId)
        return MeetingInfo(meeting)
    }

    private fun getMeetingByIdAndHandleException(meetingId: String): Meeting {
        val meeting = meetingRepo.findById(meetingId)
        if (meeting.isPresent)
            return meeting.get()
        throw EntityDoesNotExist(ErrorMessage.MEETING_DOES_NOT_EXIST)
    }

}