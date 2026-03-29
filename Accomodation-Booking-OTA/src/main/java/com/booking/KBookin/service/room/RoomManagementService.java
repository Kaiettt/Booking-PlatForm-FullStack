package com.booking.KBookin.service.room;

import com.booking.KBookin.dto.room.RoomCreateRequestDTO;
import com.booking.KBookin.dto.room.RoomResponseDTO;
import com.booking.KBookin.dto.room.RoomTypeCreateRequestDTO;
import com.booking.KBookin.entity.room.Room;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Set;

public interface RoomManagementService {
    void updateAllRooms(Set<Room> selectedRooms);
    Long createRoomType(RoomTypeCreateRequestDTO requestDTO);
    Long uploadRoomTypeMedia(Long roomTypeId, List<MultipartFile> files);
    RoomResponseDTO createRoom(RoomCreateRequestDTO requestDTO);
    List<RoomResponseDTO> createRooms(List<RoomCreateRequestDTO> requestDTOs);
}
