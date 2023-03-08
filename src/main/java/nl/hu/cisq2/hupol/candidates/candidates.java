package nl.hu.cisq2.hupol.candidates;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class candidates {
    Repo repo;
    public candidates(Repo repo) {
        this.repo = repo;
    }
    @PostMapping("/candidates")
    public void Importcandidatelist(@RequestParam("file") MultipartFile f) { // upload file in body form data
         try {
            if(f!=null && !f.isEmpty()) {
                List<Candidate> candies = new ArrayList<>();
                String c = new String(f.getBytes(), StandardCharsets.UTF_8); // file to string
                // parse csv into candidates to insert into db
                String[] rs = c.split("\r\n|\r|\n");

                for (int rnum = 0; rnum < rs.length; rnum++) {
                    if(!rs[rnum].isEmpty()) {
                        if (!rs[rnum].isBlank() && rs[rnum].length() > 0) {
                            String[] cols = rs[rnum].split(";");
                            candies.add(new Candidate(cols[0], Long.parseLong(cols[1]), cols[2], cols[3]));
                        } else {
                            continue;
                        }
                    } else {
                        continue;
                    }
                }
                for (int i = 0; i < candies.size(); i++) {
                    repo.save(candies.get(i));
                }
                } else {throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing file");}
         }
         catch (IOException e) {throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot read file");}
         catch (DateTimeParseException | NumberFormatException e) {throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File incorrect format"); }
    }
}
