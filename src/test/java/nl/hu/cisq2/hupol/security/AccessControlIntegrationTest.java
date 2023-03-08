package nl.hu.cisq2.hupol.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class AccessControlIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("showing election results requires login")
    public void electionResults() throws Exception {
        mockMvc.perform(get("/election/0/results"))
                .andExpect(status().isForbidden());
    }


    @Test
    @DisplayName("importing votes requires login")
    public void importVotes() throws Exception {
        mockMvc.perform(post("/votes"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("importing candidates requires login")
    public void importCandidates() throws Exception {
        mockMvc.perform(post("/candidates"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("promoting admin requires login")
    public void promoteAdmin() throws Exception {
        mockMvc.perform(post("/auth/admin/some-user"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("demoting admin requires login")
    public void demoteAdmin() throws Exception {
        mockMvc.perform(delete("/auth/admin/some-user"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("we need a bearer token as an authentication header")
    public void authHeader() throws Exception {
        var promotion = delete("/auth/admin/some-user")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "1234");
        mockMvc.perform(promotion)
                .andExpect(status().isForbidden());
    }
}
