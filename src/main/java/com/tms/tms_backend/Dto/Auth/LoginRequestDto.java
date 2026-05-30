package com.tms.tms_backend.Dto.Auth;

import lombok.Data;

@Data
public class LoginRequestDto {

    private String email;

    private String password;
}