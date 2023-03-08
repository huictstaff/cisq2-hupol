package nl.hu.cisq2.hupol.candidates;

import nl.hu.cisq2.hupol.Fixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class candidatesIntegrationTest {
    @Autowired
    private Repo candidateRepository;

    @Autowired
    private candidates candidateController;

    @Test
    @DisplayName("import a list of candidates")
    void importList() {
        // Given a multipart-file containing a list of candidates we want to import
        String input = Fixture.fromFile("candidates-01.csv");
        MultipartFile file = new MockMultipartFile("candidates-01.csv", input.getBytes());

        // When we import the file
        candidateController.Importcandidatelist(file);

        // Then the candidates in that file should show up in the database
        var allCandidates = candidateRepository.findAll();
        var expectedCandidates = List.of(
                new Candidate("2a3fva21", 1L, "Elron Husky", "MarsRed"),
                new Candidate("83a231as", 1L, "Oshari Letux", "Greens"),
                new Candidate("ya3fva21", 1L, "Henry Fleming", "Rovers"),
                new Candidate("2a3fva21", 2L, "Elron Husky", "MarsRed")
        );

        for (int i = 0; i < allCandidates.size(); i++) {
            assertEquals(expectedCandidates.get(i), allCandidates.get(i));
        }
    }

    @Test
    @DisplayName("don't accept incorrectly formatted number")
    void incorrectlyFormattedNumber() {
        // Given a multipart-file containing a badly formatted number
        String input = "s0m31d;1a;Some Name;Some Faction;";
        MultipartFile file = new MockMultipartFile("file.csv", input.getBytes());

        // When we import that poorly formatted file
        Executable action = () -> candidateController.Importcandidatelist(file);

        // Then we should get a ResponseStatusException
        var exception = assertThrows(ResponseStatusException.class, action);

        // And we should get a BAD REQUEST status code
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    @DisplayName("don't accept empty file")
    void emptyFile() {
        // Given an empty file
        String input = "";
        MultipartFile file = new MockMultipartFile("file.csv", input.getBytes());

        // When we import that poorly formatted file
        Executable action = () -> candidateController.Importcandidatelist(file);

        // Then we should get a ResponseStatusException
        var exception = assertThrows(ResponseStatusException.class, action);

        // And we should get a BAD REQUEST status code
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    @DisplayName("read error is converted correctly")
    void ioError() throws IOException {
        // Given a multipart-file that has a read error
        MultipartFile file = Mockito.mock(MultipartFile.class);
        Mockito.when(file.getBytes()).thenThrow(new IOException());

        // When we import the file
        Executable action = () -> candidateController.Importcandidatelist(file);

        // Then we expect the correct error
        var exception = assertThrows(ResponseStatusException.class, action);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @BeforeEach
    @AfterEach
    void cleanUpBeforeAndAfterEachCase() {
        candidateRepository.deleteAll();
    }
}
