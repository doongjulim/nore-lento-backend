package io.github.dongjulim.security.service;

import io.github.dongjulim.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username){
        var user = userRepository.findByUsernameAndDeleteCheck(username,false)
                .orElseThrow( () -> new UsernameNotFoundException("유효하지 않은 정보"));
        var role =  List.of(new SimpleGrantedAuthority(user.getRole().value()));

        return new User(user.getUsername(), user.getPassword(),role);
    }
}
