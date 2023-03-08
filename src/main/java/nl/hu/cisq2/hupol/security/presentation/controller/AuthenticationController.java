package nl.hu.cisq2.hupol.security.presentation.controller;

import nl.hu.cisq2.hupol.security.application.AdminService;
import nl.hu.cisq2.hupol.security.application.AuthenticationService;
import nl.hu.cisq2.hupol.security.domain.UserProfile;
import nl.hu.cisq2.hupol.security.domain.exception.UserAlreadyExists;
import nl.hu.cisq2.hupol.security.domain.exception.UserNotFound;
import nl.hu.cisq2.hupol.security.presentation.dto.RegisterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    private final AuthenticationService authService;
    private final AdminService adminService;

    public AuthenticationController(AuthenticationService authService, AdminService adminService) {
        this.authService = authService;
        this.adminService = adminService;
    }

    @PostMapping("register")
    public UserProfile register(@Validated @RequestBody RegisterRequest credentials) {
        try {
            return this.authService.register(credentials.username(), credentials.password());
        } catch (UserAlreadyExists exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }
    }

    @PostMapping("admin/{username}")
    public void promoteAdmin(@PathVariable String username) {
        try {
            this.adminService.promote(username);
        } catch (UserNotFound exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("admin/{username}")
    public void demoteAdmin(@PathVariable String username) {
        try {
            this.adminService.demote(username);
        } catch (UserNotFound exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
