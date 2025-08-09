package com.cinnamonbay.backend.security.user;

import lombok.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.userdetails.*;
import com.cinnamonbay.backend.model.User;

import java.util.*;
import java.util.stream.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HotelUserDetails implements UserDetails{
    private Long id;
    private String email;
    private String password;
    private Collection<GrantedAuthority> authorities;

    public static HotelUserDetails buildUserDetails(User user){
        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return new HotelUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}