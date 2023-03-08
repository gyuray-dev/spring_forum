package dev.gyuray.forum.service.user;

import dev.gyuray.forum.domain.Address;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class UserForm {
    private String name;
    private String city;
    private String street;
    private String zipcode;
    private String email;
    private LocalDateTime regDate;
}
