package junyharang.jwt.study.spring_boot_jwt_tutorial.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component public class TokenProvider implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHOTITIES_KEY = "auth";

    private final String secret;

    private final long tokenValidityInmilliseconds;

    private Key key;

    public TokenProvider(   // Bean 생성 뒤 의존성 주입
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInmilliseconds) {
        this.secret = secret;
        this.tokenValidityInmilliseconds = tokenValidityInmilliseconds * 1000;
    } // TokenProvider(@Value("${jwt.secret}") String secret, @Value("${jwt.token-validity-inseconds}") long tokenValidityInmilliseconds) 끝

    @Override
    // 위에서 주입 받은 secret 값을 Base64 Decode를 통해 key 변수에 할당
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);

        this.key = Keys.hmacShaKeyFor(keyBytes);
    } // afterPropertiesSet() 끝

    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        // application.yml에서 설정했던 만료시간을 설정하고 Token 생성
        long now = new Date().getTime();
        Date validity = new Date(now + this.tokenValidityInmilliseconds);

        // jwt 생성 뒤 반환
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHOTITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    } // createToken(Authentication authentication) 끝

    // Token에 담겨있는 정보를 이용하여 Authentication 객체를 반환하는 Method 생성
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHOTITIES_KEY).toString().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    } // getAuthentication(String token) 끝

    public boolean validateToken(String token) { // 토큰의 유효성 검증 수행 하는 Method
        try {
            // 토큰을 매개변수로 받아 parserBuilder를 통해 파싱을 하고, 발생하는 Exception을 잡아 문제가 있으면 false, 정상이면 true 반환
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명이에요ㅠ");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 입니다!");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 입니다!");
        } catch (IllegalArgumentException e) {
            logger.info("JWT가 잘못 되었습니다!");
        } // try - catch 끝

        return false;

    } // validateToken(String token) 끝


} // class 끝
