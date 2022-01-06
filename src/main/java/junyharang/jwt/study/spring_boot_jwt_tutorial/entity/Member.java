package junyharang.jwt.study.spring_boot_jwt_tutorial.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "member")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(name = "member_name", length = 50, unique = true)
    private String memberName;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "nickname", length = 50)
    private String nickName;

    @Column(name = "activated")
    private boolean activated;

    @ManyToMany
    @JoinTable(
            name = "member_authority",
            joinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "member_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;
} // class 끝
