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
        model.addAttribute("studierenden", new StudierendeDto());
        return "studierenden";
    }

    @GetMapping("/{id}")
    String find(final Model model,
                @PathVariable("id") final long id) {
        model.addAttribute("studierende",
                studierendeEntityService
                        .findById(id)
                        .orElseThrow(IllegalArgumentException::new));
        return "studierende-detail";
    }

    @PostMapping("")
    String createStudierende(@ModelAttribute("studierenden") final StudierendeDto studierendenTemplate) {
        studierendeEntityService.createFrom(studierendenTemplate);
        // causes a page reload
        return "redirect:/ui/studierenden";
    }

    /*@DeleteMapping("/{id}")
    String deleteStudierende(@PathVariable("id") final long id) {
        Optional<Studierende> studierende = studierendeEntityService.findById(id);
        //Source from https://stackoverflow.com/questions/42977137/creating-an-object-from-optionalobject
        Seminarraum a = studierende.stream().findFirst().orElse(null);
        studierendeEntityServicee.delete(a);
        return "redirect:/ui/seminarraeume";
    }*/

}
