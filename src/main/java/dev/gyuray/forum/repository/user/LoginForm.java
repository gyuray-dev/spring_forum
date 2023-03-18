package dev.gyuray.forum.repository.user;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginForm {
    private String loginId;
    private String password;
}
