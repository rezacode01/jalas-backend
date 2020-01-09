package ir.seven.jalas.controllers

import ir.seven.jalas.services.ParticipationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/meetings/{meetingId}/participation")
class ParticipationController {

    @Autowired
    private lateinit var participationService: ParticipationService

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER') and @authorizationService.isMeetingCreator(#principal.name, #meetingId)")
    fun addParticipant(
            @AuthenticationPrincipal principal: Principal,
            @PathVariable meetingId: String,
            @RequestParam username: String
    ) {
        participationService.addParticipantToMeeting(username, meetingId)
    }
}