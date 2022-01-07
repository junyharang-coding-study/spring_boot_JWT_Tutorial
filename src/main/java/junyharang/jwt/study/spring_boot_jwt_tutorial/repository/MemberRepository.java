package junyharang.jwt.study.spring_boot_jwt_tutorial.repository;

import junyharang.jwt.study.spring_boot_jwt_tutorial.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @EntityGraph(attributePaths = "authorities") // Query 수행 시 Lazy 조회가 아니고, Eager 조회로 Authorities 정보를 같이 가져오게 하기 위한 설정
    // Member의 이름을 가져오는데, 권한 정보를 같이 가져오게 하기 위함.
    Optional<Member> findOneWithAuthoritiesByMemberName(String memberName);

} // interface 끝
