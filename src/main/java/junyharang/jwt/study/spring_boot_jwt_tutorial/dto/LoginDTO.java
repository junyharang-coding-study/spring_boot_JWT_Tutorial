package junyharang.jwt.study.spring_boot_jwt_tutorial.dto;

import javax.validation.constraints.NotNull;
import lombok.*;

import javax.validation.constraints.Size;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class LoginDTO {

    @NotNull @Size(min = 3, max = 50) private String userName;
    @NotNull @Size(min = 3, max = 100) private String password;

} // class 끝
