package nl.hu.cisq2.hupol.security.presentation;

import nl.hu.cisq2.hupol.security.application.AdminService;
import nl.hu.cisq2.hupol.security.application.AuthenticationService;
import nl.hu.cisq2.hupol.security.data.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static nl.hu.cisq2.hupol.security.domain.Role.ROLE_ADMIN;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerWebTest {
    @Autowired
    private AdminService adminService;

    @Autowired
    AuthenticationService authService;

    @Autowired
    private UserRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("register")
    public void register() throws Exception {
        String username = "testuser";
        String password = "testpassword";

        String jsonBody = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);

        var request = post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.authorities[0].authority").value("ROLE_USER"));
    }

    @Test
    @DisplayName("cannot register if user exists")
    public void cannotRegister() throws Exception {
        String username = "testuser";
        String password = "testpassword";

        String jsonBody = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);

        var request = post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody);

        mockMvc.perform(request)
                .andExpect(status().isOk());

        mockMvc.perform(request)
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("promote admin")
    public void promoteAdmin() throws Exception {
        // Given an admin and a user
        adminService.registerNewUserAsAdmin("admin", "admin");
        authService.register("testuser", "testuser");

        // When we are logged in as admin
        String loginBody = "{\"username\": \"admin\", \"password\": \"admin\"}";
        var login = post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginBody);
        MvcResult loginResult = mockMvc.perform(login)
                .andExpect(status().isOk())
                .andReturn();

        String authHeader = loginResult.getResponse().getHeader("Authorization");

        // Then we can promote the user to an admin
        var promotion = post("/auth/admin/testuser")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authHeader);
        mockMvc.perform(promotion)
                .andExpect(status().isOk());

        // And the user has the correct role
        var roles = authService
                .login("testuser", "testuser")
                .authorities()
                .stream()
                .map(GrantedAuthority::getAuthority).toList();

        assertTrue(roles.contains(ROLE_ADMIN.toString()));
    }

    @Test
    @DisplayName("cannot promote if user not found")
    public void cannotPromoteIfNotFound() throws Exception {
        // Given an admin and no user
        adminService.registerNewUserAsAdmin("admin", "admin");

        // When we are logged in as admin
        String loginBody = "{\"username\": \"admin\", \"password\": \"admin\"}";
        var login = post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginBody);
        MvcResult loginResult = mockMvc.perform(login)
                .andExpect(status().isOk())
                .andReturn();

        String authHeader = loginResult.getResponse().getHeader("Authorization");

        // Then we cannot promote an unknown user to an admin
        var promotion = post("/auth/admin/testuser")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authHeader);
        mockMvc.perform(promotion)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("demote admin")
    public void demoteAdmin() throws Exception {
        // Given two admins
        adminService.registerNewUserAsAdmin("admin", "admin");
        adminService.registerNewUserAsAdmin("testuser", "testuser");

        // When we are logged in as admin
        String loginBody = "{\"username\": \"admin\", \"password\": \"admin\"}";
        var login = post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginBody);
        MvcResult loginResult = mockMvc.perform(login)
                .andExpect(status().isOk())
                .andReturn();

        String authHeader = loginResult.getResponse().getHeader("Authorization");

        // Then we can demote the admin
        var promotion = delete("/auth/admin/testuser")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authHeader);
        mockMvc.perform(promotion)
                .andExpect(status().isOk());

        // And the user has the correct role
        var roles = authService
                .login("testuser", "testuser")
                .authorities()
                .stream()
                .map(GrantedAuthority::getAuthority).toList();

        assertFalse(roles.contains(ROLE_ADMIN.toString()));
    }

    @Test
    @DisplayName("cannot demote if user not found")
    public void cannotDemoteIfNotFound() throws Exception {
        // Given an admin and no user
        adminService.registerNewUserAsAdmin("admin", "admin");

        // When we are logged in as admin
        String loginBody = "{\"username\": \"admin\", \"password\": \"admin\"}";
        var login = post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginBody);
        MvcResult loginResult = mockMvc.perform(login)
                .andExpect(status().isOk())
                .andReturn();

        String authHeader = loginResult.getResponse().getHeader("Authorization");

        // Then we cannot demote user to an admin
        var promotion = delete("/auth/admin/testuser")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authHeader);
        mockMvc.perform(promotion)
                .andExpect(status().isNotFound());
    }

    @BeforeEach
    @AfterEach
    void cleanUpBeforeAndAfterEachCase() {
        repository.deleteAll();
    }
}
