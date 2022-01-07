package junyharang.jwt.study.spring_boot_jwt_tutorial.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class MemberDTO {

    @NotNull @Size(min = 3, max = 50) private String memberName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) @NotNull @Size(min = 3, max = 100) private String password;

    @NotNull @Size(min = 3, max = 50) private String nickName;

} // class 끝
