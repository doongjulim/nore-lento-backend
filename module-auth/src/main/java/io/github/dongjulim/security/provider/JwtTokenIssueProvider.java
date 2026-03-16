package io.github.dongjulim.security.provider;

import io.github.dongjulim.config.PasswordConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Import(PasswordConfig.class)
public class JwtTokenIssueProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var username = (String) authentication.getPrincipal();
        var password = (String) authentication.getCredentials();

        return authenticate(username,password);
    }

    private UsernamePasswordAuthenticationToken authenticate(String username, String password) {
        UserDetails user = userDetailsService.loadUserByUsername(username);
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new BadCredentialsException("유효하지 않은 정보.");
        }

        if (!user.isEnabled()){
            throw new BadCredentialsException("유효하지 않은 정보.");
        }

        return new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
