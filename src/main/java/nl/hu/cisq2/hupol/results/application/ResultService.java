package nl.hu.cisq2.hupol.results.application;

import nl.hu.cisq2.hupol.candidates.Candidate;
import nl.hu.cisq2.hupol.candidates.Repo;
import nl.hu.cisq2.hupol.results.domain.ResultPerCandidate;
import nl.hu.cisq2.hupol.votes.VRepo;
import nl.hu.cisq2.hupol.votes.Vote;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResultService {
    private final Repo candidatesRepository;
    private final VRepo votesRepository;

    public ResultService(Repo candidatesRepository, VRepo votesRepository) {
        this.candidatesRepository = candidatesRepository;
        this.votesRepository = votesRepository;
    }

    public List<ResultPerCandidate> calculateResultsPerCandidate(Long electionId) {
        List<ResultPerCandidate> results = new ArrayList<>();

        List<Candidate> candidates = candidatesRepository.findAll();
        List<Vote> votes = votesRepository.findAll();

        for (Vote vote : votes) {
            if (!vote.hasElectionId(electionId)) {
                continue;
            }

            for (Candidate candidate : candidates) {
                if (!candidate.hasElectionId(electionId)) {
                    continue;
                }

                if (vote.isForCandidate(candidate.getCandidateId())) {
                    this.countVoteForCandidate(candidate, results);
                }
            }
        }

        return results;
    }

    private void countVoteForCandidate(Candidate candidate, List<ResultPerCandidate> results) {
        boolean found = false;

        for (ResultPerCandidate result : results) {
            if (result.isForCandidate(candidate.getCandidateId())) {
                result.countVote();
                found = true;
                break;
            }
        }

        if (!found) {
            results.add(new ResultPerCandidate(candidate.getCandidateId(), candidate.getName(), candidate.getFaction(), 1L));
        }
    }
}
