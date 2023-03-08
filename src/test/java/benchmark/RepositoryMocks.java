package benchmark;

import nl.hu.cisq2.hupol.candidates.Candidate;
import nl.hu.cisq2.hupol.candidates.Repo;
import nl.hu.cisq2.hupol.votes.VRepo;
import nl.hu.cisq2.hupol.votes.Vote;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RepositoryMocks {
    private static VRepo votesInstance = null;
    private static Repo candidatesInstance = null;

    public static VRepo votes() {
        if (votesInstance == null) {
            var allVotes = randomVotes();

            votesInstance = Mockito.mock(VRepo.class);
            Mockito.when(votesInstance.findAll()).thenReturn(allVotes);
        }

        return votesInstance;
    }

    public static Repo candidates() {
        if (candidatesInstance == null) {
            var allCandidates = randomCandidates();

            candidatesInstance = Mockito.mock(Repo.class);
            Mockito.when(candidatesInstance.findAll()).thenReturn(allCandidates);
        }

        return candidatesInstance;
    }

    private static List<Candidate> randomCandidates() {
        var random = new Random();

        return IntStream.range(0, 25)
                .mapToObj(i -> new Candidate(
                        "c" + i,
                        1L,
                        "candidate" + i,
                        "faction" + random.nextInt(6)
                ))
                .collect(Collectors.toList());
    }

    private static List<Vote> randomVotes() {
        var random = new Random();
        return IntStream.range(0, 2500)
                .mapToObj(i -> new Vote(
                        1L,
                        i,
                        "c" + random.nextInt(1, 25),
                        LocalDate.of(3000, 1, 1),
                        "region" + new Random().nextInt(6)
                ))
                .collect(Collectors.toList());
    }
}
