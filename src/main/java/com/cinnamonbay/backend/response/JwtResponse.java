package com.cinnamonbay.backend.response;

import lombok.*;

import java.util.List;

/**
 * @author Simpson Alfred
 */

@Data


@NoArgsConstructor
public class JwtResponse {
    private Long id;
    private String email;
    private String token;
    private String type = "Bearer";
    private List<String> roles;

    public JwtResponse(Long id, String email, String token, List<String> roles) {
        this.id = id;
        this.email = email;
        this.token = token;
        this.roles = roles;
    }
}
