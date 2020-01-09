package ir.seven.jalas.controllers

import ir.seven.jalas.services.SlotService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/meetings/{meetingId}/slots")
class SlotController {

    @Autowired
    private lateinit var slotService: SlotService

    @DeleteMapping("/{slotId}")
    @PreAuthorize("hasRole('ROLE_USER') and @authorizationService.isMeetingCreator(#principal.name, #meetingId)")
    fun deleteSlot(
            @AuthenticationPrincipal principal: Principal,
            @PathVariable meetingId: String,
            @PathVariable slotId: String
    ) {
        slotService.deleteSlot(meetingId, slotId)
    }
}