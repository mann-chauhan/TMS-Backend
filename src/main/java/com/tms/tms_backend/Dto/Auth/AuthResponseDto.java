package com.tms.tms_backend.Dto.Auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDto {

    private String token;

    private String role;

    private String name;
}