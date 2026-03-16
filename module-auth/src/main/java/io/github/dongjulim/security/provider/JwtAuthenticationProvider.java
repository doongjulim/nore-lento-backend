package io.github.dongjulim.security.provider;

import io.github.dongjulim.domain.user.enums.Role;
import io.github.dongjulim.security.JwtAuthenticationToken;
import io.github.dongjulim.security.util.JwtUtil;
import io.github.dongjulim.security.util.dto.TokenParseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtUtil jwtUtil;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return authenticate((JwtAuthenticationToken) authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private Authentication authenticate(JwtAuthenticationToken authentication) throws AuthenticationException {
        String jwtToken = authentication.getCredentials();
        TokenParseResponse response = jwtUtil.parserToken(jwtToken);

        return new JwtAuthenticationToken(response.username(), authorities(response));
    }

    private List<SimpleGrantedAuthority> authorities(TokenParseResponse response) {
        return response.roles().stream()
                .map(Role::value)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
