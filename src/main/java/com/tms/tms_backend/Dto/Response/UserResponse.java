package com.tms.tms_backend.Dto.Response;

import lombok.Builder;

import lombok.Data;

@Data

@Builder

public class UserResponse {

    private Long id;

    private String fullName;

    private String email;

    private String phone;

    private String department;

    private String role;

    private Boolean isActive;

    private String profileImage;

}