package com.tms.tms_backend.Service.imp;

import com.tms.tms_backend.Dto.Request.AddUserRequest;
import com.tms.tms_backend.Dto.Response.ManagerResponse;
import com.tms.tms_backend.Dto.Response.UserResponse;
import com.tms.tms_backend.Entity.Role;
import com.tms.tms_backend.Entity.User;
import com.tms.tms_backend.Repository.RoleRepository;
import com.tms.tms_backend.Repository.UserRepository;
import com.tms.tms_backend.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;



@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserResponse addUser(AddUserRequest request) {

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = User.builder()
                .employeeCode(request.getEmployeeCode())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(request.getPassword())
                .phone(request.getPhone())
                .department(request.getDepartment())
                .isActive(request.getIsActive())
                .profileImage(request.getProfileImage())
                .role(role)
                .build();

        User savedUser = userRepository.save(user);

        return UserResponse.builder()
                .id(savedUser.getId())
                .employeeCode(savedUser.getEmployeeCode())
                .fullName(savedUser.getFullName())
                .email(savedUser.getEmail())
                .phone(savedUser.getPhone())
                .department(savedUser.getDepartment())
                .role(savedUser.getRole().getName().name())
                .isActive(savedUser.getIsActive())
                .profileImage(savedUser.getProfileImage())
                .build();
    }

    @Override
    public ManagerResponse getManagerByDepartment(String department) {

        User manager = userRepository
                .findByDepartmentAndRole_Name(
                        department,
                        Role.RoleName.MANAGER
                )
                .orElseThrow(() ->
                        new RuntimeException("Manager not found")
                );

        return ManagerResponse.builder()
                .id(manager.getId())
                .fullName(manager.getFullName())
                .department(manager.getDepartment())
                .build();
    }

    @Override
    public List<UserResponse> getAllUsers() {

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .employeeCode(user.getEmployeeCode())
                        .fullName(user.getFullName())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .department(user.getDepartment())
                        .role(user.getRole().getName().name())
                        .isActive(user.getIsActive())
                        .profileImage(user.getProfileImage())
                        .build()
                )
                .toList();
    }
    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        return UserResponse.builder()
                .id(user.getId())
                .employeeCode(user.getEmployeeCode())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .department(user.getDepartment())
                .role(user.getRole().getName().name())
                .isActive(user.getIsActive())
                .profileImage(user.getProfileImage())
                .build();
    }

    @Override
    public UserResponse updateUser(
            Long id,
            AddUserRequest request
    ) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() ->
                        new RuntimeException("Role not found")
                );

        // MANAGER VALIDATION

        if(role.getName() == Role.RoleName.MANAGER){

            Optional<User> existingManager =
                    userRepository.findByDepartmentAndRole_Name(
                            request.getDepartment(),
                            Role.RoleName.MANAGER
                    );

            if(existingManager.isPresent()
                    && !existingManager.get().getId().equals(user.getId())){

                throw new RuntimeException(
                        "Department already has manager"
                );
            }
        }
        user.setEmployeeCode(request.getEmployeeCode());
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());
        user.setDepartment(request.getDepartment());
        user.setRole(role);
        user.setIsActive(request.getIsActive());
        user.setProfileImage(request.getProfileImage());

        User updatedUser = userRepository.save(user);

        return UserResponse.builder()
                .id(updatedUser.getId())
                .fullName(updatedUser.getFullName())
                .email(updatedUser.getEmail())
                .phone(updatedUser.getPhone())
                .department(updatedUser.getDepartment())
                .role(updatedUser.getRole().getName().name())
                .isActive(updatedUser.getIsActive())
                .profileImage(updatedUser.getProfileImage())
                .build();
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        userRepository.delete(user);
    }
}