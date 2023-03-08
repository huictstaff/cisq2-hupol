package nl.hu.cisq2.hupol.results.presentation;

import nl.hu.cisq2.hupol.results.application.ResultService;
import nl.hu.cisq2.hupol.results.domain.ResultPerCandidate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ResultControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResultService service;

    @Test
    @DisplayName("get election results")
    void getElectionResults() throws Exception {
        Mockito.when(service.calculateResultsPerCandidate(0L))
                .thenReturn(List.of(
                        new ResultPerCandidate("32id123", "name", "faction", 1000L)
                ));

        var request = get("/election/0/results")
                .with(user("some-user"));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].candidateId").value("32id123"))
                .andExpect(jsonPath("$.[0].candidateName").value("name"))
                .andExpect(jsonPath("$.[0].faction").value("faction"))
                .andExpect(jsonPath("$.[0].votes").value(1000));
    }
}
