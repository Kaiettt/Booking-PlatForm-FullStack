package com.booking.KBookin.controller.booking;

import com.booking.KBookin.dto.booking.InventoryHoldResponse;
import com.booking.KBookin.dto.booking.LockRoomRequestDTO;
import com.booking.KBookin.dto.booking.LockRoomResponseDTO;
import com.booking.KBookin.service.booking.InventoryLockService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/lock/rooms")
public class InventoryLockController {
    private InventoryLockService inventoryLockService;
    @PostMapping
    public ResponseEntity<InventoryHoldResponse> lockRoom(@RequestBody LockRoomRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.inventoryLockService.handleLockRoom(request));
    }
}
