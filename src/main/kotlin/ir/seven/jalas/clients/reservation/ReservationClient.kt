package ir.seven.jalas.clients.reservation

import ir.seven.jalas.clients.reservation.configuration.ReservationConfiguration
import ir.seven.jalas.dto.AvailableRooms
import ir.seven.jalas.dto.ReserveInfo
import ir.seven.jalas.dto.ReserveRoomResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@FeignClient(
        name = "reserve-system",
        url = "http://5.253.27.176",
        configuration = [ReservationConfiguration::class]
)
interface ReservationClient {

    @GetMapping("/available_rooms")
    fun getAllAvailableRooms(
            @RequestParam(required = true) start: String,
            @RequestParam(required = true) end: String
    ): AvailableRooms

    @PostMapping("rooms/{roomId}/reserve")
    fun reserveRoom(
            @PathVariable roomId: Int,
            @RequestBody reserveInfo: ReserveInfo
    ): ResponseEntity<ReserveRoomResponse>
}