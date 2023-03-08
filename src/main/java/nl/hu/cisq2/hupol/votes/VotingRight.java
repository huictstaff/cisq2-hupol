package nl.hu.cisq2.hupol.votes;

import java.io.Serializable;
import java.util.Objects;

public class VotingRight implements Serializable {
    private long electionId;
    private long voterId;

    public VotingRight() {
    }

    public VotingRight(long electionId, long voterId) {
        this.electionId = electionId;
        this.voterId = voterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VotingRight that)) return false;
        return electionId == that.electionId && voterId == that.voterId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(electionId, voterId);
    }
}
