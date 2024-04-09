package nl.hu.cisq2.hupol.security.application;

import jakarta.transaction.Transactional;
import nl.hu.cisq2.hupol.security.data.UserRepository;
import nl.hu.cisq2.hupol.security.domain.Role;
import nl.hu.cisq2.hupol.security.domain.User;
import nl.hu.cisq2.hupol.security.domain.UserProfile;
import nl.hu.cisq2.hupol.security.domain.exception.UserAlreadyExists;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AuthenticationService implements UserDetailsService {
    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfile login(String username, String password) {
        User user = this.userRepository.findById(username)
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        if (!password.equals(user.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }

        return new UserProfile(user.getUsername(), user.getAuthorities());
    }

    public UserProfile register(String username, String password) {
        if (this.userRepository.existsById(username)) {
            throw new UserAlreadyExists();
        }

        List<Role> roles = new ArrayList<>();
        roles.add(Role.ROLE_USER);

        User user = new User(username, password, roles);

        this.userRepository.save(user);

        return new UserProfile(user.getUsername(), user.getAuthorities());
    }
    @Override
    public User loadUserByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
