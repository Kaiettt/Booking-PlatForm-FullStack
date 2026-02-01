package com.booking.KBookin.controller.room;

import com.booking.KBookin.dto.room.RoomCreateRequestDTO;
import com.booking.KBookin.dto.room.RoomResponseDTO;
import com.booking.KBookin.service.room.RoomService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/rooms")
@AllArgsConstructor
@RestController
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomResponseDTO> createRoom(@Valid @RequestBody RoomCreateRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roomService.createRoom(requestDTO));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<RoomResponseDTO>> createRooms(@Valid @RequestBody List<RoomCreateRequestDTO> requestDTOs) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roomService.createRooms(requestDTOs));
    }
}
