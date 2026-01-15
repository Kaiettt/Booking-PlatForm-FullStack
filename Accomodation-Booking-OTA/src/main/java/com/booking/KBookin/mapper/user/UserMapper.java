package com.booking.KBookin.mapper.user;

import com.booking.KBookin.dto.user.UserResponseDTO;
import com.booking.KBookin.entity.user.User;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO toDto(User user);
}