package junyharang.jwt.study.spring_boot_jwt_tutorial.config.util;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@NoArgsConstructor
public class SecurityUtil {

    private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    public static Optional<String> getCurrentMemberName() { // Security Context의 Authentication 객체를 이용 memberName 반환
       final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            logger.debug("Security Context에 인증 정보가 없습니다!");
            return Optional.empty();
        } // if문 끝

        String memberName = null;

        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityMember = (UserDetails) authentication.getPrincipal();

            memberName = springSecurityMember.getUsername();
        } else if (authentication.getPrincipal() instanceof String){
            memberName = (String) authentication.getPrincipal();
        } // if - else문 끝

        return Optional.ofNullable(memberName);
    } // getCurrentMemberName() 끝

} // class 끝
