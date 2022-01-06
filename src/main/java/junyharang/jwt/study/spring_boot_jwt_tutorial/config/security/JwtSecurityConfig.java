package junyharang.jwt.study.spring_boot_jwt_tutorial.config.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private TokenProvider tokenProvider;

    public JwtSecurityConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    } // JwtSecurityConfig(TokenProvider tokenProvider) 끝

    @Override
    public void configure(HttpSecurity http) throws Exception {
        JwtFilter customFilter = new JwtFilter(tokenProvider);

        // Security Logic에 Filter 등록
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    } // configure(HttpSecurity http) 끝
} // class 끝
