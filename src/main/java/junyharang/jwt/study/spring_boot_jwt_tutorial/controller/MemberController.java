package junyharang.jwt.study.spring_boot_jwt_tutorial.controller;

import junyharang.jwt.study.spring_boot_jwt_tutorial.dto.MemberDTO;
import junyharang.jwt.study.spring_boot_jwt_tutorial.entity.Member;
import junyharang.jwt.study.spring_boot_jwt_tutorial.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController @RequestMapping("/api") public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup") public ResponseEntity<Member> signup(@Valid @RequestBody MemberDTO memberDTO) {
        return ResponseEntity.ok(memberService.signup(memberDTO));
    } // signup(@Valid @RequestBody MemberDTO memberDTO) 끝

    // PreAuthorize를 통해 USER와 ADMIN 권한 모두를 호출할수 있도록 허용
    @GetMapping("/member") @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Member> getMyMemberInfo() {
        return ResponseEntity.ok(memberService.getMyMemberWithAuthorities().get());
    } // getMyMemberInfo() 끝

    // PreAuthorize를 통해 ADMIN 권한만 호출할 수 있도록 허용
    @GetMapping("/member/{memberName}") @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Member> getMemberInfo(@PathVariable String memberName) {
        return ResponseEntity.ok(memberService.getMemberWithAuthorities(memberName).get());
    } // getMemberInfo(@PathVariable String memberName) 끝
} // class 끝
