package dev.gyuray.forum.service;

import dev.gyuray.forum.domain.Address;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class UserForm {
    private String name;
    private Address address;
    private String email;
    private LocalDateTime regDate;
}
