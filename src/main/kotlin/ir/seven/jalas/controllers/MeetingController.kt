package ir.seven.jalas.controllers

import ir.seven.jalas.controllers.DTO.AvailableRooms
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/meetings")
class MeetingController {

    @GetMapping("/available_rooms")
    fun getAvailableRooms(
            @RequestParam startDate: Date,
            @RequestParam endDate: Date
    ): AvailableRooms {
        TODO("not implemented")
    }
}