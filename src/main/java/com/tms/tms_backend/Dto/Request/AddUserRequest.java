package com.tms.tms_backend.Dto.Request;

import lombok.Data;

@Data

public class AddUserRequest {

    private String fullName;

    private String email;

    private String password;

    private String phone;

    private String department;

    private Long roleId;

    private Boolean isActive;

    private String profileImage;

}