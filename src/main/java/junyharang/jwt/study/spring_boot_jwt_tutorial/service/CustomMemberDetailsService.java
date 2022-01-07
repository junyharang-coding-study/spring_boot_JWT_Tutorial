package junyharang.jwt.study.spring_boot_jwt_tutorial.service;

import junyharang.jwt.study.spring_boot_jwt_tutorial.entity.Member;
import junyharang.jwt.study.spring_boot_jwt_tutorial.repository.MemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component("memberDetailsService") public class CustomMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public CustomMemberDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    } // CustomMemberDetailsService(MemberRepository memberRepository) 끝

    @Override @Transactional
    // 이용자가 로그인시에 DB에서 이용자 정보와 권한 정보를 가져오기 위한 Method
    // 해당 정보를 기반으로 userdetails.User 객체를 생성하여 반환
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findOneWithAuthoritiesByMemberName(username).map(member -> createMember(username, member)).orElseThrow(() -> new UsernameNotFoundException(username + "해당 Member를 DB에서 찾을 수 없습니다!"));
    } // loadUserByUsername(String username) 끝

    private org.springframework.security.core.userdetails.User createMember(String membername, Member member) {
        if (!member.isActivated()) {
            throw new RuntimeException(membername + "활성화 되어 있지 않습니다!");
        } // if문 끝

        List<SimpleGrantedAuthority> grantedAuthorities = member.getAuthorities().stream().map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName())).collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(member.getMemberName(), member.getPassword(),grantedAuthorities);
    } // createMember(String membername, Member member) 끝
} // class 끝
