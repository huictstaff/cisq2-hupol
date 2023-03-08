package nl.hu.cisq2.hupol.votes;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@IdClass(VotingRight.class)
public class Vote {
    @Id
    private long electionId;

    @Id
    private long voterId;

    private String candidateId;

    private LocalDate castDate;

    private String region;

    public Vote() {}

    public Vote(long electionId, long voterId, String candidateId, LocalDate castDate, String region) {
        this.electionId = electionId;
        this.voterId = voterId;
        this.candidateId = candidateId;
        this.castDate = castDate;
        this.region = region;
    }

    public boolean hasElectionId(Long electionId) {
        return this.electionId == electionId;
    }

    public boolean isForCandidate(String id) {
        return this.candidateId.equals(id);
    }

    public long getElectionId() {
        return electionId;
    }

    public long getVoterId() {
        return voterId;
    }

    public VotingRight getVotingRight() {
        return new VotingRight(electionId, voterId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vote vote)) return false;
        return Objects.equals(castDate, vote.castDate) && Objects.equals(candidateId, vote.candidateId) && Objects.equals(region, vote.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(castDate, candidateId, region);
    }
}
