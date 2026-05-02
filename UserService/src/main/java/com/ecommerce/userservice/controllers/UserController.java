package com.ecommerce.userservice.controllers;

import com.ecommerce.userservice.dtos.SetUserRolesRequestDto;
import com.ecommerce.userservice.dtos.UserDto;
import com.ecommerce.userservice.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserDetails(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserDetails(id));
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity<UserDto> setUserRoles(@PathVariable Long id,
                                                 @RequestBody SetUserRolesRequestDto request) {
        return ResponseEntity.ok(userService.setUserRoles(id, request.getRoleIds()));
    }
}
