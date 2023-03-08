package nl.hu.cisq2.hupol.votes;

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
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class controllerIntegrationTest {
    @Autowired
    private VRepo voteRepository;

    @Autowired
    private controller voteController;

    @Test
    @DisplayName("import a list of votes")
    void importList() {
        // Given a multipart-file containing a list of votes we want to import
        String input = Fixture.fromFile("votes-01.csv");
        MultipartFile file = new MockMultipartFile("votes-01.csv", input.getBytes());

        // When we import the file
        voteController.importtvotes(file);

        // Then the votes in that file should show up in the database
        var allVotes = voteRepository.findAll();
        var expectedVotes = List.of(
                new Vote(1L, 0L, "2a3fva21", LocalDate.parse("2101-02-01"), "Chryse Planitia"),
                new Vote(1L, 1L, "83a231as", LocalDate.parse("2101-02-01"), "Aeolis Palus"),
                new Vote(1L, 2L, "ya3fva21", LocalDate.parse("2101-02-23"), "Coprates Chasma"),
                new Vote(2L, 3L, "2a3fva21", LocalDate.parse("2101-02-05"), "Medusae Fossae")
        );

        for (int i = 0; i < allVotes.size(); i++) {
            assertEquals(expectedVotes.get(i), allVotes.get(i));
        }
    }

    @Test
    @DisplayName("don't accept incorrectly formatted dates")
    void incorrectlyFormattedDate() {
        // Given a multipart-file containing a badly formatted date
        String input = "1;1;12345678;35-35-35;region";
        MultipartFile file = new MockMultipartFile("file.csv", input.getBytes());

        // When we import that poorly formatted file
        Executable action = () -> voteController.importtvotes(file);

        // Then we should get a ResponseStatusException
        var exception = assertThrows(ResponseStatusException.class, action);

        // And we should get a BAD REQUEST status code
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    @DisplayName("don't accept incorrectly formatted election id")
    void incorrectlyFormattedElectionId() {
        // Given a multipart-file containing a badly formatted id
        String input = "1abc;1;12345678;35-35-35;region";
        MultipartFile file = new MockMultipartFile("file.csv", input.getBytes());

        // When we import that poorly formatted file
        Executable action = () -> voteController.importtvotes(file);

        // Then we should get a ResponseStatusException
        var exception = assertThrows(ResponseStatusException.class, action);

        // And we should get a BAD REQUEST status code
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    @DisplayName("don't accept incorrectly formatted voter id")
    void incorrectlyFormattedVoterId() {
        // Given a multipart-file containing a badly formatted id
        String input = "1;1ax;12345678;35-35-35;region";
        MultipartFile file = new MockMultipartFile("file.csv", input.getBytes());

        // When we import that poorly formatted file
        Executable action = () -> voteController.importtvotes(file);

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
        Executable action = () -> voteController.importtvotes(file);

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
        Executable action = () -> voteController.importtvotes(file);

        // Then we expect the correct error
        var exception = assertThrows(ResponseStatusException.class, action);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @BeforeEach
    @AfterEach
    void cleanUpBeforeAndAfterEachCase() {
        voteRepository.deleteAll();
    }
}
