package dev.gyuray.forum.repository.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.*;

@Getter @Setter
public class UserForm {
    @Size(min = 2, max = 12, message = "4~12자 범위로 입력해주세요.")
    private String name;

    private String city;
    private String street;

    @Size(max = 12, message = "12자 이하로 입력해주세요.")
    private String zipcode;

    @NotBlank
    @Email
    private String email;

    @Pattern(regexp = "[a-zA-Z1-9]{4,16}", message = "아이디는 영문자 및 숫자 4~16자리로 입력해주세요.")
    private String loginId;

    @Size(min = 4, max = 16, message = "4~16자 범위로 입력해주세요.")
    private String password;
}
