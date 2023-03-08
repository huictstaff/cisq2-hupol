package nl.hu.cisq2.hupol.security.application;

import nl.hu.cisq2.hupol.security.data.UserRepository;
import nl.hu.cisq2.hupol.security.domain.Role;
import nl.hu.cisq2.hupol.security.domain.User;
import nl.hu.cisq2.hupol.security.domain.exception.UserNotFound;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static nl.hu.cisq2.hupol.security.domain.Role.ROLE_ADMIN;
import static nl.hu.cisq2.hupol.security.domain.Role.ROLE_USER;
import static org.junit.jupiter.api.Assertions.*;

class AdminServiceTest {
    @Test
    @DisplayName("create new user as admin")
    void createAdmin() {
        var repository = Mockito.mock(UserRepository.class);
        Mockito.when(repository.existsById("username"))
                .thenReturn(false);

        var service = new AdminService(repository);
        service.registerNewUserAsAdmin("username", "password");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(repository).save(userCaptor.capture());

        var roles = userCaptor
                .getValue()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        assertTrue(roles.contains(ROLE_USER.toString()));
        assertTrue(roles.contains(ROLE_ADMIN.toString()));
    }

    @Test
    @DisplayName("dont change user if already exists")
    void cannotCreateAdminIfExists() {
        var repository = Mockito.mock(UserRepository.class);
        Mockito.when(repository.existsById("username"))
                .thenReturn(true);

        var service = new AdminService(repository);
        service.registerNewUserAsAdmin("username", "password");

        Mockito.verify(repository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("promote user to admin")
    void promote() {
        List<Role> initialRoles = new ArrayList<>();
        initialRoles.add(ROLE_USER);
        var user = new User("username", "password", initialRoles);

        var repository = Mockito.mock(UserRepository.class);
        Mockito.when(repository.findById("username"))
                .thenReturn(Optional.of(user));

        var service = new AdminService(repository);
        service.promote("username");

        assertEquals(List.of(ROLE_USER, ROLE_ADMIN), user.getRoles());
    }

    @Test
    @DisplayName("cannot promote to admin if user not found")
    void cannotPromoteWhenNotFound() {
        var repository = Mockito.mock(UserRepository.class);
        Mockito.when(repository.findById("username"))
                .thenReturn(Optional.empty());

        var service = new AdminService(repository);
        Executable action = () -> service.promote("username");

        assertThrows(UserNotFound.class, action);
    }

    @Test
    @DisplayName("demote admin")
    void demote() {
        var repository = Mockito.mock(UserRepository.class);
        List<Role> initialRoles = new ArrayList<>();
        initialRoles.add(ROLE_USER);
        initialRoles.add(ROLE_ADMIN);
        var user = new User("username", "password", initialRoles);
        Mockito.when(repository.findById("username"))
                .thenReturn(Optional.of(user));

        var service = new AdminService(repository);
        service.demote("username");

        assertEquals(List.of(ROLE_USER), user.getRoles());
    }

    @Test
    @DisplayName("cannot demote admin if user not found")
    void cannotDemoteWhenNotFound() {
        var repository = Mockito.mock(UserRepository.class);
        Mockito.when(repository.findById("username"))
                .thenReturn(Optional.empty());

        var service = new AdminService(repository);
        Executable action = () -> service.demote("username");

        assertThrows(UserNotFound.class, action);
    }
}