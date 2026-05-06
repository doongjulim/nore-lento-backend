package io.github.dongjulim.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dongjulim.model.ProblemDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenIssueFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ProblemDetail problem = ProblemDetail.builder()
                .type("/api/errors/authentication-failed")
                .title("Authentication Failed")
                .status(HttpStatus.UNAUTHORIZED.value())
                .detail(exception.getMessage())
                .instance(request.getRequestURI())
                .build();

        objectMapper.writeValue(response.getWriter(), problem);
    }
}
