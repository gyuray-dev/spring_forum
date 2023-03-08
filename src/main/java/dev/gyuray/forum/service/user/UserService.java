package dev.gyuray.forum.service.user;

import dev.gyuray.forum.domain.Address;
import dev.gyuray.forum.domain.User;
import dev.gyuray.forum.repository.user.UserRepository;
import dev.gyuray.forum.repository.user.UserUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void join(UserForm userForm) {
        if (userRepository.findByName(userForm.getName()).isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }

        Address address = new Address();
        address.setCity(userForm.getCity());
        address.setStreet(userForm.getStreet());
        address.setZipcode(userForm.getZipcode());

        User user = new User();
        user.setName(userForm.getName());
        user.setAddress(address);
        user.setEmail(userForm.getEmail());
        user.setRegDate(userForm.getRegDate());
        userRepository.save(user);
    }

    public User findUser(Long userId) {
        return userRepository.findOne(userId).orElseThrow(() -> {
            throw new IllegalStateException("해당 ID를 가진 유저가 없습니다");
        });
    }

    public List<User> findUsers() {
        return userRepository.findAll();
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
