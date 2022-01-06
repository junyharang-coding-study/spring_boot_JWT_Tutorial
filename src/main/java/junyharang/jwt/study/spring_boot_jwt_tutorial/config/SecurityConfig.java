package junyharang.jwt.study.spring_boot_jwt_tutorial.config;

import junyharang.jwt.study.spring_boot_jwt_tutorial.config.security.JwtAccessDeniedHandler;
import junyharang.jwt.study.spring_boot_jwt_tutorial.config.security.JwtAuthenticationEntryPoint;
import junyharang.jwt.study.spring_boot_jwt_tutorial.config.security.JwtSecurityConfig;
import junyharang.jwt.study.spring_boot_jwt_tutorial.config.security.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)  // @PreAuthorize를 Method 단위로 추가하기 위하여 사용
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(TokenProvider tokenProvider, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    } // SecurityConfig(TokenProvider tokenProvider, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler) 끝

    @Bean public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    } // passwordEncoder() 끝

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                // h2-console 하위 모든 요청들과 파비콘 관련 요청은 Spring Secutiry 로직을 수행하지 않도록 설정
                .ignoring()
                .antMatchers(
                        "/h2-console/**","/favicon.ico"
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()   // token 방식 사용시에는 disable

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                // h2-console을 위한 설정
                .and().headers().frameOptions().sameOrigin()
                // 세션 방식을 사용하지 않기 때문에 Stateless로 설정
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and().authorizeRequests().antMatchers("/api/hello").permitAll()
                                          .antMatchers("/api/authenticate").permitAll()     // token을 받기 위한 api
                                          .antMatchers("api/signup").permitAll()            // 회원 가입을 위한 api
                .anyRequest().authenticated()    // 나머지 모든 요청은 인증을 거쳐야 허용하겠다는 의미

                // JwtFilter를 addFilterBefore로 등록했던 JwtSecurityConfig Class 적
                .and().apply(new JwtSecurityConfig(tokenProvider));
    } // configure(HttpSecurity http) 끝
} // class 끝
