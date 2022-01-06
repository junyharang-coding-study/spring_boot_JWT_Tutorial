package junyharang.jwt.study.spring_boot_jwt_tutorial.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity public class SecurityConfig extends WebSecurityConfigurerAdapter {



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
                .authorizeRequests()                               // HttpServletRequest를 사용하는 요청들에 대한 접근을 제한 하겠다는 의미
                .antMatchers("/api/hello").permitAll()  // URI /api/hello에 대해 모두 인증 없이 접근 허용하겠다는 의미
                .anyRequest().authenticated();                     // 나머지 모든 요청은 인증을 거쳐야 허용하겠다는 의미
    } // configure(HttpSecurity http) 끝
} // class 끝
