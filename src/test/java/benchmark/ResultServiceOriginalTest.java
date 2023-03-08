package benchmark;

import nl.hu.cisq2.hupol.candidates.Candidate;
import nl.hu.cisq2.hupol.candidates.Repo;
import nl.hu.cisq2.hupol.results.domain.ResultPerCandidate;
import nl.hu.cisq2.hupol.votes.VRepo;
import nl.hu.cisq2.hupol.votes.Vote;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class ResultServiceOriginalTest {
    private static final List<Candidate> CANDIDATES = List.of(
            new Candidate("c1", 1L, "candidate1", "faction1"),
            new Candidate("c2", 1L, "candidate2", "faction1"),
            new Candidate("c3", 1L, "candidate3", "faction2"),
            new Candidate("c1", 2L, "candidate1", "faction1")
    );

    private static final List<Vote> VOTES = List.of(
            new Vote(1L, 0L, "c1", LocalDate.of(3000, 1, 1), "region1"),
            new Vote(1L, 1L, "c1", LocalDate.of(3000, 1, 2), "region2"),
            new Vote(2L, 2L, "c2", LocalDate.of(3000, 1, 1), "region1"),
            new Vote(1L, 3L, "c3", LocalDate.of(3000, 1, 1), "region2")
    );

    @Test
    @DisplayName("produce results per candidate")
    void resultsPerCandidate() {
        var candidateRepository = Mockito.mock(Repo.class);
        var voteRepository = Mockito.mock(VRepo.class);
        when(candidateRepository.findAll()).thenReturn(CANDIDATES);
        when(voteRepository.findAll()).thenReturn(VOTES);

        var resultService = new ResultServiceOriginal(candidateRepository, voteRepository);
        var results = resultService.calculateResultsPerCandidate(1L);

        var expectedResults = List.of(
                new ResultPerCandidate("c1", "candidate1", "faction1", 2L),
                new ResultPerCandidate("c3", "candidate3", "faction2", 1L)
        );

        expectedResults.forEach(expected -> assertTrue(results.contains(expected)));
    }

    @Test
    @DisplayName("empty results if no candidates in election")
    void emptyResults() {
        var candidateRepository = Mockito.mock(Repo.class);
        var voteRepository = Mockito.mock(VRepo.class);
        when(candidateRepository.findAll()).thenReturn(List.of());
        when(voteRepository.findAll()).thenReturn(List.of());

        var resultService = new ResultServiceOriginal(candidateRepository, voteRepository);
        var results = resultService.calculateResultsPerCandidate(1L);

        var expectedResults = List.of();
        assertEquals(expectedResults, results);
    }
}