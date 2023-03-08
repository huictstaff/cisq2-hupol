package nl.hu.cisq2.hupol.results.presentation;

import nl.hu.cisq2.hupol.results.application.ResultService;
import nl.hu.cisq2.hupol.results.domain.ResultPerCandidate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ResultController {
    private final ResultService service;

    public ResultController(ResultService service) {
        this.service = service;
    }

    @GetMapping("/election/{electionId}/results")
    public List<ResultPerCandidate> getResultsPerCandidate(@PathVariable Long electionId) {
        return service.calculateResultsPerCandidate(electionId);
    }
}
