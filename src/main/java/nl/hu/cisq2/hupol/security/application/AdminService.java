package nl.hu.cisq2.hupol.security.application;

import nl.hu.cisq2.hupol.security.data.UserRepository;
import nl.hu.cisq2.hupol.security.domain.Role;
import nl.hu.cisq2.hupol.security.domain.User;
import nl.hu.cisq2.hupol.security.domain.exception.UserNotFound;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static nl.hu.cisq2.hupol.security.domain.Role.ROLE_ADMIN;
import static nl.hu.cisq2.hupol.security.domain.Role.ROLE_USER;

@Service
public class AdminService {
    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerNewUserAsAdmin(String username, String password) {
        if (this.userRepository.existsById(username)) {
            return;
        }

        List<Role> roles = new ArrayList<>();
        roles.add(ROLE_USER);
        roles.add(ROLE_ADMIN);
        User user = new User(username, password, roles);

        this.userRepository.save(user);
    }

    public void promote(String username) {
        User user = this.userRepository.findById(username)
                .orElseThrow(UserNotFound::new);

        user.addRole(ROLE_ADMIN);

        this.userRepository.save(user);
    }

    public void demote(String username) {
        User user = this.userRepository.findById(username)
                .orElseThrow(UserNotFound::new);

        user.removeRole(ROLE_ADMIN);

        this.userRepository.save(user);
    }
}
