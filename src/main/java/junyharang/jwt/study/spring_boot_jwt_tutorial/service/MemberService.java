package junyharang.jwt.study.spring_boot_jwt_tutorial.service;

import junyharang.jwt.study.spring_boot_jwt_tutorial.config.util.SecurityUtil;
import junyharang.jwt.study.spring_boot_jwt_tutorial.dto.MemberDTO;
import junyharang.jwt.study.spring_boot_jwt_tutorial.entity.Authority;
import junyharang.jwt.study.spring_boot_jwt_tutorial.entity.Member;
import junyharang.jwt.study.spring_boot_jwt_tutorial.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional public Member signup(MemberDTO memberDTO) {  // 회원가입 로직 수행
        // memberDTO안에 UserName이 DB에 없으면 Authority와 Member 정보를 생성해서 MemberRepository를 통해 DB에 저장
        if (memberRepository.findOneWithAuthoritiesByMemberName(memberDTO.getMemberName()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 계정입니다!");
        } // if문 끝

        // 가입하는 회원의 권한 정보 생성
       Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

       // 권한 정보도 추가하여 Member 정보를 만든다.
        Member member = Member.builder()
                .memberName(memberDTO.getMemberName())
                .password(passwordEncoder.encode(memberDTO.getPassword()))
                .nickName(memberDTO.getNickName())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return memberRepository.save(member);
    } // signup(MemberDTO memberDTO) 끝

    // Member와 권한 정보를 가져오는 Method
    // memberName을 기준으로 해당 member의 member 객체와 권한 정보를 가져 온다.
    @Transactional(readOnly = true) public Optional<Member> getMemberWithAuthorities(String memberName) {
        return memberRepository.findOneWithAuthoritiesByMemberName(memberName);
    } // getMemberWithAuthorities(String memberName) 끝

    // Member와 권한 정보를 가져오는 Method
    // 현재 SecurityContext에 저장 되어 있는 memberName에 해당(getCurrentMemberName)하는 member 정보와 권한 정보만 가져 온다.
    @Transactional(readOnly = true) public Optional<Member> getMyMemberWithAuthorities() {
        return SecurityUtil.getCurrentMemberName().flatMap(memberRepository::findOneWithAuthoritiesByMemberName);
    } // getMyMemberWithAuthorities() 끝
} // class 끝
