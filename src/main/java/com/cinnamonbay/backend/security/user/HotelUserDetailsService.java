package com.cinnamonbay.backend.security.user;

import com.cinnamonbay.backend.repository.*;
import lombok.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;
import com.cinnamonbay.backend.model.User;

@Service
@RequiredArgsConstructor
public class HotelUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return HotelUserDetails.buildUserDetails(user);
    }
}