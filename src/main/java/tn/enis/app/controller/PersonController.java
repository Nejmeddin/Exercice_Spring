package tn.enis.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tn.enis.app.entities.Person;
import tn.enis.app.service.PersonService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    /* ───── Liste complète ───── */

    @GetMapping("/persons")
    public String persons(Model model) {
        model.addAttribute("persons", personService.findAll());
        return "persons";
    }

    /* ───── Recherche avec critères ET ───── */

    @GetMapping("/persons/search")
    public String search(
            @RequestParam(name = "nom", required = false, defaultValue = "") String nom,
            @RequestParam(name = "prenom", required = false, defaultValue = "") String prenom,
            @RequestParam(name = "civilite", required = false) Person.Civility civilite,
            Model model) {

        boolean hasNom = nom != null && !nom.isBlank();
        boolean hasPrenom = prenom != null && !prenom.isBlank();
        boolean hasCivilite = civilite != null;
        boolean searched = hasNom || hasPrenom || hasCivilite;

        List<Person> results = personService.search(nom, prenom, civilite);

        model.addAttribute("nom", nom);
        model.addAttribute("prenom", prenom);
        model.addAttribute("civilite", civilite);
        model.addAttribute("civilites", Person.Civility.values());
        model.addAttribute("persons", results);
        model.addAttribute("searched", searched);

        return "recherche";
    }

    /* ───── Détail / Modification (GET) ───── */

    @GetMapping("/persons/{id}")
    public String details(@PathVariable("id") Long id, Model model) {
        Person person = personService.findById(id);

        if (person == null) {
            return "redirect:/persons/search";
        }

        model.addAttribute("person", person);
        model.addAttribute("civilites", Person.Civility.values());
        return "details";
    }

    /* ───── Modification (POST) ───── */

    @PostMapping("/persons/{id}")
    public String update(
            @PathVariable("id") Long id,
            @Valid Person person,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            person.setId(id);
            model.addAttribute("person", person);
            model.addAttribute("civilites", Person.Civility.values());
            return "details";
        }

        Person updated = personService.update(id, person);

        if (updated == null) {
            return "redirect:/persons/search";
        }

        redirectAttributes.addFlashAttribute("success", "Personne modifiée avec succès !");
        return "redirect:/persons/" + id;
    }

    /* ───── Ajout (GET) ───── */

    @GetMapping("/persons/new")
    public String addForm(Model model) {
        model.addAttribute("person", new Person());
        model.addAttribute("civilites", Person.Civility.values());
        return "add";
    }

    /* ───── Ajout (POST) ───── */

    @PostMapping("/persons/new")
    public String add(
            @Valid Person person,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("civilites", Person.Civility.values());
            return "add";
        }

        personService.save(person);
        redirectAttributes.addFlashAttribute("success", "Personne ajoutée avec succès !");
        return "redirect:/persons/search";
    }

    /* ───── Suppression (POST) ───── */

    @PostMapping("/persons/{id}/delete")
    public String delete(
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes) {

        boolean deleted = personService.deleteById(id);

        if (deleted) {
            redirectAttributes.addFlashAttribute("success", "Personne supprimée avec succès !");
        }

        return "redirect:/persons/search";
    }
}