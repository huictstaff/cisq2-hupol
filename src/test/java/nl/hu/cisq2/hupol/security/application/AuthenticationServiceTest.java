package nl.hu.cisq2.hupol.security.application;

import nl.hu.cisq2.hupol.security.data.UserRepository;
import nl.hu.cisq2.hupol.security.domain.Role;
import nl.hu.cisq2.hupol.security.domain.User;
import nl.hu.cisq2.hupol.security.domain.UserProfile;
import nl.hu.cisq2.hupol.security.domain.exception.UserAlreadyExists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthenticationServiceTest {
    @Test
    @DisplayName("user can successfully login")
    void login() {
        String username = "username";
        String password = "password";
        List<Role> roles = List.of(Role.ROLE_USER);
        var user = new User(username, password, roles);

        var repository = Mockito.mock(UserRepository.class);
        Mockito.when(repository.findById(username))
                .thenReturn(Optional.of(user));

        var service = new AuthenticationService(repository);
        UserProfile profile = service.login(username, password);

        List<String> profileRoles = profile.authorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        List<String> expectedRoles = roles
                .stream()
                .map(Role::toString)
                .toList();

        assertEquals(username, profile.username());
        assertEquals(expectedRoles, profileRoles);
    }

    @Test
    @DisplayName("cannot login if user not exists")
    void loginNotExists() {
        var repository = Mockito.mock(UserRepository.class);
        Mockito.when(repository.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        var service = new AuthenticationService(repository);
        Executable action = () -> service.login("username", "password");

        assertThrows(BadCredentialsException.class, action);
    }

    @Test
    @DisplayName("cannot login if wrong password")
    void wrongPassword() {
        String username = "username";
        String password = "password";
        List<Role> roles = List.of(Role.ROLE_USER);
        var user = new User(username, password, roles);

        var repository = Mockito.mock(UserRepository.class);
        Mockito.when(repository.findById(username))
                .thenReturn(Optional.of(user));

        var service = new AuthenticationService(repository);
        Executable action = () -> service.login(username, "wrong!");

        assertThrows(BadCredentialsException.class, action);
    }

    @Test
    @DisplayName("user can register an account")
    void registration() {
        String username = "username";
        String password = "password";
        List<Role> roles = List.of(Role.ROLE_USER);
        var user = new User(username, password, roles);

        var repository = Mockito.mock(UserRepository.class);
        Mockito.when(repository.existsById(username))
                .thenReturn(false);

        var service = new AuthenticationService(repository);
        service.register(username, password);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(repository).save(userCaptor.capture());

        assertEquals(user, userCaptor.getValue());
    }

    @Test
    @DisplayName("cannot register if user exists")
    void registrationExists() {
        var repository = Mockito.mock(UserRepository.class);
        Mockito.when(repository.existsById("username"))
                .thenReturn(true);

        var service = new AuthenticationService(repository);
        Executable action = () -> service.register("username", "password");

        assertThrows(UserAlreadyExists.class, action);
    }
}