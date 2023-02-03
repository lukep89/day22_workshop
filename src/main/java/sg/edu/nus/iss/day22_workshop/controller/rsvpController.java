package sg.edu.nus.iss.day22_workshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletResponse;
import sg.edu.nus.iss.day22_workshop.model.RSVP;
import sg.edu.nus.iss.day22_workshop.repo.RsvpRepoImpl;

@Controller

public class rsvpController {

    @Autowired
    RsvpRepoImpl rsvpRepo;

    @GetMapping("/")
    public String form(Model model) {
        model.addAttribute("rsvp", new RSVP());
        return "form";
    }

    @PostMapping("/new")
    public String saveRsvp(RSVP r, Model model, HttpServletResponse response) {

        String inputEmail = r.getEmail();
        System.out.println("inputEmail >>>> " + inputEmail);

        /*
         * if user fill form.
         * system use email to check back with database if email exist.
         * if email exist, exisitng record updated.
         * else email dont exist, new record created.
         */

        RSVP found = rsvpRepo.findByEmail(inputEmail);

        if (found != null) {

            r.setId(found.getId());
            System.out.println("TO UPDATE >>>>> " + r.toString());
            Boolean isUpdate = rsvpRepo.update(r);

            if (isUpdate) {

                return "updated";
            } else {
                return "customerror";
            }
        }

        rsvpRepo.save(r);
        System.out.println("CREATED >>>>> " + r.toString());
        response.setStatus(HttpServletResponse.SC_CREATED);
        return "created";
    }

}
