package dev.gyuray.forum.service.user;

import dev.gyuray.forum.domain.Address;
import dev.gyuray.forum.domain.User;
import dev.gyuray.forum.repository.user.UserForm;
import dev.gyuray.forum.repository.user.UserUpdateDTO;
import dev.gyuray.forum.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    Map<String, UserForm> getData() {
        Map<String, UserForm> userForms = new HashMap<>();

        UserForm userFormA = new UserForm();
        userFormA.setName("userA");
        userFormA.setCity("cityA");
        userFormA.setStreet("streetA");
        userFormA.setZipcode("12345");
        userFormA.setEmail("userA@email.com");

        UserForm userFormB = new UserForm();
        userFormB.setName("userB");
        userFormB.setCity("cityB");
        userFormB.setStreet("streetB");
        userFormB.setZipcode("54321");
        userFormB.setEmail("userB@email.com");

        userForms.put(userFormA.getName(), userFormA);
        userForms.put(userFormB.getName(), userFormB);

        return userForms;
    }

    @Test
    void joinAndWithdraw() {
        UserForm userFormA = getData().get("userA");

        //가입
        Long userId = userService.join(userFormA);
        User foundUser = userService.findUser(userId);
        Assertions.assertEquals("userA", foundUser.getName());

        //탈퇴
        userService.withdraw(foundUser.getId());
        Assertions.assertThrows(IllegalStateException.class, () -> {
            userService.findUser(foundUser.getId());
        });
    }

    @Test
    void findUserByName() {
        UserForm userFormA = getData().get("userA");
        userService.join(userFormA);
        Optional<User> foundUser = userService.findUserByName(userFormA.getName());

        Assertions.assertEquals(userFormA.getName(), foundUser.orElse(null).getName());
    }

    @Test
    void findAll() {
        UserForm userFormA = getData().get("userA");
        UserForm userFormB = getData().get("userB");
        userService.join(userFormA);
        userService.join(userFormB);

        List<User> users = userService.findUsers();
        Assertions.assertEquals(2, users.size());
    }

    @Test
    void duplicateUserNameCheck() {
        UserForm userFormA = getData().get("userA");
        UserForm userFormA2 = getData().get("userA");

        userService.join(userFormA);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            userService.join(userFormA2);
        });
    }

    @Test
    void updateUser() {
        UserForm userFormA = getData().get("userA");

        Long userId = userService.join(userFormA);
        User foundUser = userService.findUser(userId);

        UserUpdateDTO updateDTO = new UserUpdateDTO();
        Address address = new Address();
        address.setCity("newCity");
        updateDTO.setAddress(address);
        updateDTO.setId(userId);

        userService.updateUser(updateDTO);

        Assertions.assertEquals(updateDTO.getAddress().getCity(), foundUser.getAddress().getCity());
    }
}