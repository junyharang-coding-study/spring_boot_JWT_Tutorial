package junyharang.jwt.study.spring_boot_jwt_tutorial.config.security;

// 유효한 자격증명을 제공하지 않고, 접근하려 할 때 401(Unauthorized Error)를 반환할 Class

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    } // commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) 끝
} // class 끝
