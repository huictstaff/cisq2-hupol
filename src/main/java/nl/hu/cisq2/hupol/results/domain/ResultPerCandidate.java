package nl.hu.cisq2.hupol.results.domain;

import java.util.Objects;

public class ResultPerCandidate {
    private final String candidateId;
    private final String candidateName;
    private final String faction;
    private Long votes;

    public ResultPerCandidate(String candidateId, String candidateName, String faction, Long votes) {
        this.candidateId = candidateId;
        this.candidateName = candidateName;
        this.faction = faction;
        this.votes = votes;
    }

    public void countVote() {
        this.votes++;
    }

    public boolean isForCandidate(String id) {
        return this.candidateId.equals(id);
    }

    public String getCandidateId() {
        return candidateId;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public String getFaction() {
        return faction;
    }

    public Long getVotes() {
        return votes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResultPerCandidate that)) return false;
        return Objects.equals(candidateId, that.candidateId) && Objects.equals(candidateName, that.candidateName) && Objects.equals(faction, that.faction) && Objects.equals(votes, that.votes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(candidateId, candidateName, faction, votes);
    }
}
