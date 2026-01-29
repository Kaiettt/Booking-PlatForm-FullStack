package com.booking.KBookin.controller.room;

import com.booking.KBookin.repository.projection.room.RoomTypeHostProjection;
import com.booking.KBookin.service.room.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/room-type")
@AllArgsConstructor
@RestController
public class RoomTypeController {
    private final RoomService roomService;
}
