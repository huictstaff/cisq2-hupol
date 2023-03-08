package nl.hu.cisq2.hupol.votes;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class controller {
    VRepo repo;

    public controller(VRepo repo) {
        this.repo = repo;
    }

    @PostMapping("/votes")
    public void importtvotes(@RequestParam("file") MultipartFile f) { // upload file in body form data
        try {
            if(f!=null && !f.isEmpty()) {
                List<Vote> vs = new ArrayList<>();
                String c = new String(f.getBytes(), StandardCharsets.UTF_8); // file to string
                // parse csv into votes to insert into db
                String[] rs = c.split("\r\n|\r|\n");

                for (int rnum = 0; rnum < rs.length; rnum++) {
                    if(!rs[rnum].isEmpty()){
                        if(!rs[rnum].isBlank()&&rs[rnum].length()>0){
                            String[] cols = rs[rnum].split(";");
                            vs.add(new Vote(Long.parseLong(cols[0]), Long.parseLong(cols[1]), cols[2], LocalDate.parse(cols[3]), cols[4]));                        } else {
                            continue;
                        }
                    } else {
                        continue;
                    }
                }
                for (int i = 0; i < vs.size(); i++) {
                    if (!repo.existsById(vs.get(i).getVotingRight())) repo.save(vs.get(i));
                }
            } else {throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing file");}
        }
        catch (IOException e) {throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot read file");}
        catch (DateTimeParseException | NumberFormatException e) {throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File incorrect format: " + e.getMessage()); }
    }
}
