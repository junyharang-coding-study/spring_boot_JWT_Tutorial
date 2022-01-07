package junyharang.jwt.study.spring_boot_jwt_tutorial.controller;

import junyharang.jwt.study.spring_boot_jwt_tutorial.config.security.JwtFilter;
import junyharang.jwt.study.spring_boot_jwt_tutorial.config.security.TokenProvider;
import junyharang.jwt.study.spring_boot_jwt_tutorial.dto.LoginDTO;
import junyharang.jwt.study.spring_boot_jwt_tutorial.dto.TokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController @RequestMapping("/api") public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    @PostMapping("/authenticate") public ResponseEntity<TokenDTO> authorize(@Valid @RequestBody LoginDTO loginDTO) {    // loginDTO 안에는 MemberName과 password 존재
        // LoginDTO의 MemberName, password를 매개변수로 받아 UsernamePasswordAuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUserName(), loginDTO.getPassword());
        // authenticationToken을 이용하여 Authentication 객체를 생성하는데, authenticate Method 실행 시 loadUserByUsername Method 호출
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // Authentication 객체를 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        // Authentication 객체를 createToken Method를 통해 JWT Token 생성
        String jwt = tokenProvider.createToken(authenticate);

        HttpHeaders httpHeaders = new HttpHeaders();
        // JWT는 Response Header에도 넣어주고,
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        // TokenDTO를 이용하여 Response Body에도 넣어 반환
        return new ResponseEntity<>(new TokenDTO(jwt), httpHeaders, HttpStatus.OK);

    } // authorize(@Valid @RequestBody LoginDTO loginDTO) 끝
} // class 끝
