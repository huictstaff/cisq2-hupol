package nl.hu.cisq2.hupol.candidates;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CandidateTest {
    @Test
    @DisplayName("a candidate's candidacy depends on its id and an election id")
    void candidacy() {
        var candidateId = "a1b2c3";
        var electionId = 123L;
        var candidate = new Candidate(candidateId, electionId, "name", "faction");

        var candidacy = candidate.getCandidacy();
        var expected = new Candidacy(electionId, candidateId);

        assertEquals(expected, candidacy);
    }

    @Test
    @DisplayName("equality is handled correctly")
    void equality() {
        EqualsVerifier.simple().forClass(Candidate.class).verify();
    }
}
