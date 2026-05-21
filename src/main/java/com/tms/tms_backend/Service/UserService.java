package com.tms.tms_backend.Service;

import com.tms.tms_backend.Dto.Request.AddUserRequest;

import com.tms.tms_backend.Dto.Response.ManagerResponse;
import com.tms.tms_backend.Dto.Response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse addUser(AddUserRequest request);

    ManagerResponse getManagerByDepartment(String department);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    UserResponse updateUser(
            Long id,
            AddUserRequest request);

    void deleteUser(Long id);
}