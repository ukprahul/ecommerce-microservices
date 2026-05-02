package com.ecommerce.userservice.services;

import com.ecommerce.userservice.dtos.UserDto;
import com.ecommerce.userservice.models.Role;
import com.ecommerce.userservice.models.User;
import com.ecommerce.userservice.repositories.RoleRepository;
import com.ecommerce.userservice.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public UserDto getUserDetails(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return UserDto.from(user.get());
    }

    public UserDto setUserRoles(Long userId, List<Long> roleIds) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        List<Role> roles = roleRepository.findAllByIdIn(roleIds);
        User user = userOptional.get();
        user.setRoles(new HashSet<>(roles));
        return UserDto.from(userRepository.save(user));
    }
}
