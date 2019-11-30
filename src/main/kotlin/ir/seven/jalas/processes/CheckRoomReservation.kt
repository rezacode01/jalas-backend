package ir.seven.jalas.processes

import ir.seven.jalas.DTO.ReserveInfo
import ir.seven.jalas.clients.reservation.ReservationClient
import ir.seven.jalas.enums.MeetingStatus
import ir.seven.jalas.services.MeetingService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@Profile("local")
class CheckRoomReservation {

    @Autowired
    private lateinit var meetingService: MeetingService

    @Autowired
    private lateinit var reservationClient: ReservationClient

    val logger = LoggerFactory.getLogger(CheckRoomReservation::class.java)

    // delay is set to 5s due to test -- Of course it's not feasible in production
    @Scheduled(fixedDelay = 5000)
    fun reserveRoomForSubmittedMeetings() {

        logger.info("Start checking room reservation process")

        meetingService.getAllMeetings().forEach { meeting ->
            if (meeting.state == MeetingStatus.ROOM_SUBMITTED) {
                try {
                    val response = reservationClient.reserveRoom(
                            roomId = meeting.room ?: return@forEach,
                            reserveInfo = ReserveInfo(
                                    meeting.creator.fullname,
                                    meeting.selectedSlot?.from ?: return@forEach,
                                    meeting.selectedSlot?.to ?: return@forEach
                            )
                        )

                    logger.info(response.message)
                    logger.info("-> Reserve room ${meeting.room} for meeting ${meeting.id}")
                } catch (exp: Exception) {
                    logger.error("-> Failed to reserve room ${meeting.room} for meeting ${meeting.id}")
                    logger.error(exp.printStackTrace().toString())
                }
            }
        }

        logger.info("End checking room reservation process")
    }
}