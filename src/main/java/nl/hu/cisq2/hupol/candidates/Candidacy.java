package nl.hu.cisq2.hupol.candidates;

import java.io.Serializable;
import java.util.Objects;

public class Candidacy implements Serializable {
    private long electionId;
    private String candidateId;

    public Candidacy() {
    }

    public Candidacy(Long electionId, String candidateId) {
        this.electionId = electionId;
        this.candidateId = candidateId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Candidacy that)) return false;
        return Objects.equals(electionId, that.electionId) && Objects.equals(candidateId, that.candidateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(electionId, candidateId);
    }
}
