package com.ecommerce.userservice.security;

import com.ecommerce.userservice.models.Role;
import org.springframework.security.core.GrantedAuthority;

public class CustomSpringGrantedAuthority implements GrantedAuthority {

    private final Role role;

    public CustomSpringGrantedAuthority(Role role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role.getRole();
    }
}
