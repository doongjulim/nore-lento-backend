package io.github.dongjulim.security.util;

import io.github.dongjulim.domain.user.enums.Role;
import io.github.dongjulim.security.exception.JwtExpiredTokenException;
import io.github.dongjulim.security.util.dto.TokenParseResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class JwtUtil {

    private final SecretKey key;
    private final long expirationTime;
    private final String issuer;

    public JwtUtil(
            @Value("${jwt.token.secret-key}") String key,
            @Value("${jwt.token.expTime}") long expirationTime,
            @Value("${jwt.token.issuer}") String issuer) {
        this.key = Keys.hmacShaKeyFor(key.getBytes());
        this.expirationTime = expirationTime;
        this.issuer = issuer;
    }

    public String createToken(String username, Collection<GrantedAuthority> authorities) {
        LocalDateTime issuedAt = LocalDateTime.now();
        LocalDateTime expiredAt = issuedAt.plusMinutes(expirationTime);

        return Jwts.builder()
                .addClaims(createClaims(username, authorities))
                .setIssuer(issuer)
                .setIssuedAt(toDate(issuedAt))
                .setExpiration(toDate(expiredAt))
                .signWith(key)
                .compact();
    }

    public TokenParseResponse parserToken(String token) throws BadCredentialsException, JwtExpiredTokenException {
        try {
            return tokenParserResponse(
                    Jwts.parserBuilder()
                            .setSigningKey(key)
                            .build()
                            .parseClaimsJws(token)
            );
        } catch (SignatureException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("유효하지 않은 토큰 정보", ex);
        } catch (ExpiredJwtException expiredEx) {
            throw new JwtExpiredTokenException("토근 기간 만료 ", expiredEx);
        }
    }

    @SuppressWarnings("unchecked")
    private TokenParseResponse tokenParserResponse(Jws<Claims> claimsJws) {
        String username = claimsJws.getBody().getSubject();
        List<String> roles = claimsJws.getBody().get("roles", List.class);

        return new TokenParseResponse(username, roles.stream().map(Role::of).toList());
    }

    private Claims createClaims(String username, Collection<GrantedAuthority> authorities) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", authorities.stream().map(Object::toString).toList());
        return claims;
    }

    private Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
