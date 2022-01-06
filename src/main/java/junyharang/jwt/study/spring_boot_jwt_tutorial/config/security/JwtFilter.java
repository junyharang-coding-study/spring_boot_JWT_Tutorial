package junyharang.jwt.study.spring_boot_jwt_tutorial.config.security;

// JWT를 위한 커스텀 필터인 JwtFilter Class

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    } // JwtFilter(TokenProvider tokenProvider) 끝

    @Override
    // 실제 Filtering Logic은 doFilter 내부에 작성
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException { // JWT 인증정보를 SecurityContext에 저장하는 역할
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        // request에서 Token을 받아 jwt에 저장
      String jwt = resolveToken(httpServletRequest);

        String requestURI = httpServletRequest.getRequestURI();

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) { //tokenProvider.validateToken을 통해 jwt에 유효성 검사

            // Token이 정상이라면 jwt에서 Authentication을 받아 authentication에 저장
            Authentication authentication = tokenProvider.getAuthentication(jwt);

            // SecurityContext에 setter를 통해 set을 해 준다.
            SecurityContextHolder.getContext().setAuthentication(authentication);

            logger.debug("Security Context에 '{}' 인증 정보를 저장 하였습니다!, uri : {}", authentication.getName(), requestURI);
        } else {
            logger.debug("유효한 JWT가 없습니다!, uri: {}", requestURI);
        } // if - else 문 끝

        chain.doFilter(request, response);
    } // doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 끝

    // Request Header에서 Token 정보를 꺼내오기 위한 resolveToken Method
    private String resolveToken(HttpServletRequest request) {
        // request header에서 Token 정보를 꺼내 bearerToken에 저장
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        } // if문 끝

        return null;
    } // resolveToken(HttpServletRequest request) 끝
} // class 끝
