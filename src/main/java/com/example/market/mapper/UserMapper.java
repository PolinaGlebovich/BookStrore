package com.example.market.mapper;

import com.example.market.dto.UserDto;
import com.example.market.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper (componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User userDtoToUser(UserDto userDto);

}
