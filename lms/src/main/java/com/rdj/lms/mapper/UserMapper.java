package com.rdj.lms.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.rdj.lms.dto.response.UserResponse;
import com.rdj.lms.entity.User;

@Component
public class UserMapper {

    // Entity → Response DTO
    public UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }

    // List Entity → List Response DTO
    public List<UserResponse> toResponseList(List<User> users) {
        List<UserResponse> list = new ArrayList<UserResponse>();
        for (User user : users) {
            list.add(toResponse(user));
        }
        return list;
    }
}
