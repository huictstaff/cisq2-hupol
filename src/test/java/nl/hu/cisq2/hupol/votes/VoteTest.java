package nl.hu.cisq2.hupol.votes;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class VoteTest {
    @Test
    @DisplayName("equality is handled correctly")
    void equality() {
        EqualsVerifier.forClass(Vote.class).verify();
    }
}
