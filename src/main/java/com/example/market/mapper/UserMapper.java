package com.example.market.mapper;

import com.example.market.dto.request.SignInRequest;
import com.example.market.dto.request.SignUpRequest;
import com.example.market.dto.request.UserRequest;
import com.example.market.dto.response.UserResponse;
import com.example.market.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "role", source = "roles")
    User toEntity(SignUpRequest signUpRequest);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", ignore = true)
    void updateUserFromRequest(UserRequest userRequest, @MappingTarget User user);

    @Mapping(target = "role", source = "roles")
    User toEntity(UserRequest userRequest);

    @Mapping(target = "roles", source = "role")
    @Mapping(target = "cartId", source = "cart.id")
    UserResponse toDto(User user);

    @Mapping(target = "role", ignore = true)
    User toEntity(SignInRequest signInRequest);

    default void updateRole(UserRequest userRequest, @MappingTarget User user) {
        if (userRequest.getRoles() != null) {
            user.setRole(userRequest.getRoles());
        }
    }
}
