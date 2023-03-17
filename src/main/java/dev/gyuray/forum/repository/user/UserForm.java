package dev.gyuray.forum.repository.user;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserForm {
    private String name;
    private String city;
    private String street;
    private String zipcode;
    private String email;
    private String password;
}
