package com.booking.KBookin.controller.room;

import com.booking.KBookin.dto.room.RoomTypeCreateRequestDTO;
import com.booking.KBookin.repository.projection.room.RoomTypeHostProjection;
import com.booking.KBookin.service.room.RoomService;
import jakarta.mail.Multipart;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequestMapping("/room-type")
@AllArgsConstructor
@RestController
public class RoomTypeController {
    private final RoomService roomService;
    @PostMapping
    public ResponseEntity<Void> createRoomType(@Valid @RequestBody RoomTypeCreateRequestDTO requestDTO){
        Long createdRoomType = this.roomService.createRoomType(requestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(("/{id}"))
                .buildAndExpand(createdRoomType)
                .toUri();
        return ResponseEntity.created(location).build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<RoomTypeHostProjection> getRoomTypeById(@PathVariable Long id){
        return ResponseEntity.ok(this.roomService.fetchRoomTypeById(id));
    }

    @PostMapping("/{id}/media") // Changed from /uploads to /media (Noun-based REST naming)
    public ResponseEntity<Void> uploadRoomTypeMedia(
            @PathVariable Long id,
            @RequestPart("files") List<MultipartFile> files) throws BadRequestException {

        if (files == null || files.isEmpty()) {
            throw new BadRequestException("At least one file is required");
        }
        this.roomService.uploadRoomTypeMedia(id, files);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

}
