package nl.hu.cisq2.hupol.votes;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VRepo extends JpaRepository<Vote, VotingRight> {
}
