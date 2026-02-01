package com.booking.KBookin.service.room.impl;

import com.booking.KBookin.dto.room.RoomCreateRequestDTO;
import com.booking.KBookin.dto.room.RoomResponseDTO;
import com.booking.KBookin.dto.room.RoomTypeCreateRequestDTO;
import com.booking.KBookin.dto.room.RoomTypeFilterDTO;
import com.booking.KBookin.entity.inventory.InventoryHold;
import com.booking.KBookin.entity.property.Property;
import com.booking.KBookin.entity.room.Room;
import com.booking.KBookin.entity.room.RoomAmenity;
import com.booking.KBookin.entity.room.RoomFacility;
import com.booking.KBookin.entity.room.RoomType;
import com.booking.KBookin.enumerate.property.PropertyStatus;
import com.booking.KBookin.exception.BusinessProcessException;
import com.booking.KBookin.repository.booking.InventoryHoldRepository;
import com.booking.KBookin.repository.booking.RoomInventoryRepository;
import com.booking.KBookin.repository.projection.room.RoomTypeHostProjection;
import com.booking.KBookin.repository.property.PropertyRepository;
import com.booking.KBookin.repository.room.RoomRepository;
import com.booking.KBookin.repository.room.RoomTypeRepository;
import com.booking.KBookin.repository.room.RoomTypeSpecifications;
import com.booking.KBookin.service.media.MediaService;
import com.booking.KBookin.service.room.RoomService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RoomServiceImpl implements RoomService {
    private RoomTypeRepository roomTypeRepository;
    private RoomRepository roomRepository;
    private RoomInventoryRepository roomInventoryRepository;
    private InventoryHoldRepository inventoryHoldRepository;
    private PropertyRepository propertyRepository;
    private MediaService mediaService;
    @Override
    public List<Room> findAvaibaleRoomsByRoomTypeIds(List<Long> roomTypeIds) {
        return this.roomRepository.findAvailableByRoomTypeIds(roomTypeIds);
    }

    @Override
    public void updateAllRooms(Set<Room> selectedRooms) {
        this.roomRepository.saveAll(selectedRooms);
    }

    @Override
    public void releaseAllRoomLock(Long userId) {
        InventoryHold inventoryHold = this.inventoryHoldRepository.findTopByUser_IdOrderByCreatedAtDesc(userId);
        if(inventoryHold == null){
            throw new EntityNotFoundException("Hold not found");
        }
        this.inventoryHoldRepository.deleteById(inventoryHold.getId());
    }

    @Override
    public void extendRoomLock(Long userId) {
        InventoryHold inventoryHold =
                this.inventoryHoldRepository.findByUserId(userId, PageRequest.of(0, 1))
                        .stream()
                        .findFirst()
                        .orElse(null);

        if(inventoryHold == null){
            throw new EntityNotFoundException("Room Lock not found");
        }
        inventoryHold.extendLock();
        this.inventoryHoldRepository.save(inventoryHold);
    }

    @Transactional
    @Override
    public void releaseRoomInventory(Integer quantity, Long roomTypeId, LocalDate checkIn, LocalDate checkOut) {
        this.roomInventoryRepository.updateAfterReleaseHolds(quantity,roomTypeId,checkIn,checkOut);
    }

    @Override
    public List<RoomTypeHostProjection> fetchPropertyRoomsById(long propertyId, RoomTypeFilterDTO filter) {
        Specification<RoomType> spec = RoomTypeSpecifications.withFilters(propertyId,filter);
        return this.roomTypeRepository.findBy(spec, q -> q.as(RoomTypeHostProjection.class).all());
    }

    @Override
    public Long createRoomType(RoomTypeCreateRequestDTO requestDTO) {
        Property property = this.propertyRepository.findById(requestDTO.getPropertyId())
                .orElseThrow(() -> new EntityNotFoundException("Property not found"));
        if(property.getStatus() == PropertyStatus.ACCEPTED){
            Set<RoomAmenity> roomAmenities = requestDTO.getAmenityIds().stream()
                    .map(id -> RoomAmenity.builder().id(id).build())
                    .collect(Collectors.toSet());
            Set<RoomFacility> roomFacilities = requestDTO.getFacilityIds().stream()
                    .map(id -> RoomFacility.builder().id(id).build())
                    .collect(Collectors.toSet());
            RoomType roomType = RoomType.builder()
                    .property(property)
                    .name(requestDTO.getName())
                    .maxAdults(requestDTO.getMaxAdults())
                    .maxChildren(requestDTO.getMaxChildren())
                    .maxGuest(requestDTO.getMaxGuest())
                    .sizeM2(requestDTO.getSizeM2())
                    .bedType(requestDTO.getBedType())
                    .viewType(requestDTO.getViewType())
                    .smokingAllowed(requestDTO.getSmokingAllowed())
                    .totalRooms(requestDTO.getTotalRooms())
                    .amenities(roomAmenities)
                    .facilities(roomFacilities)
                    .build();
                    return this.roomTypeRepository.save(roomType).getId();
        }
        else {
            throw new BusinessProcessException("Cannot create new room type. Property is not active");
        }
    }

    @Override
    public RoomTypeHostProjection fetchRoomTypeById(Long id) {
        return this.roomTypeRepository.findRoomTypeById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room type not found"));
    }

    @Override
    public Long uploadRoomTypeMedia(Long roomTypeId, List<MultipartFile> files) {
        RoomType roomType = this.roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Room Type not found"));
        this.mediaService.updateRoomTypeImages(roomType,files);
        return this.roomTypeRepository.save(roomType).getId();
    }

    @Override
    public RoomResponseDTO createRoom(RoomCreateRequestDTO requestDTO) {
        RoomType roomType = this.roomTypeRepository.findById(requestDTO.getRoomTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Room Type not found"));


        Room savedRoom = this.roomRepository.save(processCreatRoom(requestDTO,roomType) );

        return processToRoomResponse(savedRoom,roomType);
    }

    @Override
    @Transactional
    public List<RoomResponseDTO> createRooms(List<RoomCreateRequestDTO> requestDTOs) {
        if (requestDTOs == null || requestDTOs.isEmpty()) {
            return List.of();
        }

        Long roomTypeId = requestDTOs.get(0).getRoomTypeId();

        RoomType roomType = this.roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Room Type not found"));

        List<Room> rooms = requestDTOs.stream()
                .map(roomRequest -> this.processCreatRoom(roomRequest, roomType))
                .toList();

        List<Room> savedRooms = this.roomRepository.saveAll(rooms);

        return savedRooms.stream()
                .map(room -> this.processToRoomResponse(room, roomType))
                .toList();
    }

    public Room processCreatRoom(RoomCreateRequestDTO requestDTO, RoomType roomType) {
        Room room = Room.builder()
                .roomNumber(requestDTO.getRoomNumber())
                .roomType(roomType)
                .build();

        if (requestDTO.getRoomStatus() != null) {
            room.setRoomStatus(requestDTO.getRoomStatus());
        }
        return room;
    }

    public RoomResponseDTO processToRoomResponse(Room room, RoomType roomType) {
        return RoomResponseDTO.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .roomStatus(room.getRoomStatus())
                .roomTypeId(roomType.getId())
                .roomTypeName(roomType.getName())
                .lastCleanedAt(room.getLastCleanedAt())
                .build();
    }
}
