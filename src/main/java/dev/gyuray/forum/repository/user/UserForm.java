package dev.gyuray.forum.repository.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter @Setter
public class UserForm {
    // TODO - 회원가입 validation 추가
    private String name;
    private String city;
    private String street;
    private String zipcode;
    private String email;
    private String loginId;
    private String password;
}
