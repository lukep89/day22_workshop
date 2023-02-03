package sg.edu.nus.iss.day22_workshop.restcontroller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.day22_workshop.model.RSVP;
import sg.edu.nus.iss.day22_workshop.repo.RsvpRepoImpl;

/*
 * GET /api/rsvps
 * GET /api/rsvps?q=fred
 * POST /api/rsvps
 * PUT /api/rsvps/{id}
 * GET /api/rsvps/count
 */

//  service class was left out.

@RestController
@RequestMapping
public class RestRsvpController {

    @Autowired
    RsvpRepoImpl rsvpRepo;

    @GetMapping("/api/rsvps")
    public ResponseEntity<List<RSVP>> getAllRsvp() {

        List<RSVP> listRsvp = new ArrayList<>();
        listRsvp = rsvpRepo.findAll();

        if (listRsvp.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(listRsvp);
    };

    @GetMapping("/api/rsvp")
    public ResponseEntity<List<RSVP>> getRsvpByName(@RequestParam("q") String name) {

        List<RSVP> listRsvp = new ArrayList<>();
        listRsvp = rsvpRepo.findByName(name);

        if (listRsvp.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(listRsvp);
    }

    @PostMapping("/api/rsvp")
    public ResponseEntity<String> saveRsvp(@RequestBody RSVP rsvp) { // change return from Boolean to String becuase you
                                                                     // want to create a string response

        try {
            RSVP r = rsvp;
            Boolean saved = rsvpRepo.save(r);

            if (saved) {
                return ResponseEntity.status(HttpStatus.CREATED).body("RSVP record created");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("RSVP not record created");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server fail to process saveRSVP");
        }

    }

    @PutMapping("/api/rsvp/{id}")
    public ResponseEntity<String> updateRsvp(@PathVariable("id") Integer id, @RequestBody RSVP rsvp) { // the
                                                                                                       // requestBody is
                                                                                                       // used because
                                                                                                       // youre going
                                                                                                       // to pump in
                                                                                                       // a model to
                                                                                                       // update
                                                                                                       // properties

        RSVP found = rsvpRepo.findById(id);

        Boolean result = false;
        if (found != null) {

            result = rsvpRepo.update(rsvp);
        }

        if (result) {
            return ResponseEntity.status(HttpStatus.CREATED).body("RSVP record updated");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("RSVP not record updated");
        }
    }

    @GetMapping("api/rsvps/count")
    public ResponseEntity<Integer> getRsvpCount() {

        Integer count = rsvpRepo.countAll();

        return ResponseEntity.status(HttpStatus.OK).body(count);
    }

    @PostMapping("/api/rsvps/batchinsert")
    public ResponseEntity<String> saveRsvpBatch(@RequestBody List<RSVP> rsvp) {

        // try {
        List<RSVP> r = new ArrayList<>();
        r = rsvp;
        int[] saved = rsvpRepo.batchInsert(r);

        // if (saved) {
        // return ResponseEntity.status(HttpStatus.CREATED).body("RSVP record created");
        // } else {
        // return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("RSVP not record
        // created");
        // }
        // } catch (Exception e) {
        // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server
        // fail to process saveRSVP");
        // }

        return null;
    }

}
