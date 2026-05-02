package com.ecommerce.userservice.dtos;

import com.ecommerce.userservice.models.Role;
import com.ecommerce.userservice.models.User;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String email;
    private Set<Role> roles;

    public static UserDto from(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles());
        return dto;
    }
}
