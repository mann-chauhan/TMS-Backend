package com.tms.tms_backend.Controller;

import com.tms.tms_backend.Dto.Response.ManagerResponse;
import com.tms.tms_backend.Dto.Request.AddUserRequest;
import com.tms.tms_backend.Dto.Response.UserResponse;
import com.tms.tms_backend.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponse addUser(@RequestBody AddUserRequest request){

        return userService.addUser(request);
    }

    @GetMapping("/manager/{department}")
    public ManagerResponse getManagerByDepartment(
            @PathVariable String department
    ){

        return userService.getManagerByDepartment(department);
    }

    @GetMapping
    public List<UserResponse> getAllUsers(){

        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id){

        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(
            @PathVariable Long id,
            @RequestBody AddUserRequest request
    ){

        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id
    ){

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

}
