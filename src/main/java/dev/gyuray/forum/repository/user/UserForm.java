package dev.gyuray.forum.repository.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@Getter @Setter
public class UserForm {
    @NotBlank
    private String name;
    private String city;
    private String street;
    private String zipcode;
    @NotBlank
    private String email;
    @NotBlank
    private String loginId;
    @NotBlank
    private String password;
}
