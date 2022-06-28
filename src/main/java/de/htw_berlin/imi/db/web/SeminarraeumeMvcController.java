package de.htw_berlin.imi.db.web;

import de.htw_berlin.imi.db.entities.Seminarraum;
import de.htw_berlin.imi.db.services.SeminarraumEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller()
@RequestMapping(path = "/ui/seminarraeume")
public class SeminarraeumeMvcController {

    @Autowired
    SeminarraumEntityService seminarraumEntityService;

    @GetMapping
    String findAll(final Model model) {
        model.addAttribute("seminarraeume", seminarraumEntityService.findAll());
        // empty template object that accepts fiel values from
        // the HTML form when new office room objetcs are created
        model.addAttribute("Seminarraum", new SeminarraeumeDto());
        return "seminarraeume";
    }

    @GetMapping("/{id}")
    String find(final Model model,
                @PathVariable("id") final long id) {
        model.addAttribute("Seminarraum",
                seminarraumEntityService
                        .findById(id)
                        .orElseThrow(IllegalArgumentException::new));
        return "seminarraum-detail";
    }

    @PostMapping("")
    String createSeminarraeume(@ModelAttribute("Seminarraum") final SeminarraeumeDto seminarRaeumeTemplate) {
        seminarraumEntityService.createFrom(seminarRaeumeTemplate);
        // causes a page reload
        return "redirect:/ui/seminarraeume";
    }

    @DeleteMapping("/{id}")
    String deleteSeminarraeume(@PathVariable("id") final long id) {
        Optional<Seminarraum> seminarraum = seminarraumEntityService.findById(id);
        //Source from https://stackoverflow.com/questions/42977137/creating-an-object-from-optionalobject
        Seminarraum a = seminarraum.stream().findFirst().orElse(null);
        seminarraumEntityService.delete(a);
        return "redirect:/ui/seminarraeume";
    }

}
