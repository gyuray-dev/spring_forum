package dev.gyuray.forum.service;

import dev.gyuray.forum.domain.Address;
import dev.gyuray.forum.domain.User;
import dev.gyuray.forum.repository.user.UserForm;
import dev.gyuray.forum.repository.user.UserRepository;
import dev.gyuray.forum.repository.user.UserUpdateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long join(UserForm userForm) {
        Address address = new Address();
        address.setCity(userForm.getCity());
        address.setStreet(userForm.getStreet());
        address.setZipcode(userForm.getZipcode());

        User user = new User();
        user.setName(userForm.getName());
        user.setAddress(address);
        user.setEmail(userForm.getEmail());
        user.setLoginId(userForm.getLoginId());
        user.setPassword(userForm.getPassword());
        userRepository.save(user);

        return user.getId();
    }

    public boolean isUsableLoginId(String loginId) {
        return userRepository.findByLoginId(loginId).isEmpty();
    }
    public boolean isUsableName(String name) {
        return userRepository.findByName(name).isEmpty();
    }

    public boolean isUsableEmail(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    public User findUser(Long userId) {
        return userRepository.findOne(userId).orElseThrow(() -> {
            throw new IllegalStateException("해당 ID를 가진 유저가 없습니다");
        });
    }

    public Optional<User> findUserByName(String userName) {
        List<User> foundUser = userRepository.findByName(userName);
        if (foundUser == null) {
            throw new IllegalStateException("해당 ID를 가진 유저가 없습니다");
        }

        return foundUser.stream().findAny();
    }

    public List<User> findUsers() {
        return userRepository.findAll();
    }

    public User login(String loginId, String password) {
        return userRepository.findByLoginId(loginId).stream()
                .filter(user -> user.getPassword().equals(password))
                .findAny().orElse(null);
    }

    @Transactional
    public void updateUser(UserUpdateDTO userUpdateDTO) {
        userRepository.update(userUpdateDTO);
    }

    @Transactional
    public void withdraw(Long userId) {
        userRepository.delete(userId);
    }

}
