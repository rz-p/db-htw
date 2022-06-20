package de.htw_berlin.imi.db.web;

import de.htw_berlin.imi.db.services.StudierendeEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller()
@RequestMapping(path = "/ui/studierenden")
public class StudierendeMvcController {

    @Autowired
    StudierendeEntityService studierendeEntityService;

    @GetMapping
    String findAll(final Model model) {
        model.addAttribute("studierenden", studierendeEntityService.findAll());
        // empty template object that accepts fiel values from
        // the HTML form when new office room objetcs are created
        model.addAttribute("Studierende", new StudierendeDto());
        return "studierenden";
    }

    @GetMapping("/{id}")
    String find(final Model model,
                @PathVariable("id") final long id) {
        model.addAttribute("Studierende",
                studierendeEntityService
                        .findById(id)
                        .orElseThrow(IllegalArgumentException::new));
        return "studierende-detail";
    }

    @PostMapping("")
    String createStudierende(@ModelAttribute("Studierende") final StudierendeDto studierendeTemplate) {
        studierendeEntityService.createFrom(studierendeTemplate);
        // causes a page reload
        return "redirect:/ui/studierenden";
    }

}
