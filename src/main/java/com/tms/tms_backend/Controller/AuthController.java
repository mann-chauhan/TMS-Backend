package com.tms.tms_backend.Controller;

import com.tms.tms_backend.Dto.Auth.AuthResponseDto;
import com.tms.tms_backend.Dto.Auth.LoginRequestDto;
import com.tms.tms_backend.Entity.User;
import com.tms.tms_backend.Repository.UserRepository;
import com.tms.tms_backend.security.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final UserRepository userRepository;

    private final JwtService jwtService;

    @PostMapping("/login")
    public AuthResponseDto login(
            @RequestBody LoginRequestDto dto
    ) {

        System.out.println(dto.getEmail());
        System.out.println(dto.getPassword());

        User user = userRepository
                .findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid Email"
                        )
                );

        // TEMP PASSWORD CHECK
        // Later BCrypt

        if (!user.getPassword()
                .equals(dto.getPassword())) {

            throw new RuntimeException(
                    "Invalid Password"
            );
        }

        String token =
                jwtService.generateToken(
                        user.getEmail(),
                        user.getRole()
                                .getName()
                                .name(),
                        user.getId()
                );

        return AuthResponseDto.builder()

                .token(token)

                .role(
                        user.getRole()
                                .getName()
                                .name()
                )

                .name(
                        user.getFullName()
                )
                .userId(
                        user.getId()
                )

                .build();
    }
}
