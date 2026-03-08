package tn.enis.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tn.enis.app.entities.Person;
import tn.enis.app.repository.PersonRepository;
import tn.enis.app.repository.PersonSpecification;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PersonController {

    private final PersonRepository personRepository;

    /* ───── Liste complète ───── */

    @GetMapping("/persons")
    public String persons(Model model) {
        model.addAttribute("persons", personRepository.findAll());
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

        List<Person> results;

        if (searched) {
            Specification<Person> spec = Specification.where(null);

            if (hasNom) {
                spec = spec.and(PersonSpecification.nomContains(nom));
            }
            if (hasPrenom) {
                spec = spec.and(PersonSpecification.prenomContains(prenom));
            }
            if (hasCivilite) {
                spec = spec.and(PersonSpecification.civiliteEquals(civilite));
            }

            results = personRepository.findAll(spec);
        } else {
            results = Collections.emptyList();
        }

        // mémoriser les critères
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
        Person person = personRepository.findById(id).orElse(null);
        if (person == null) {
            return "redirect:/persons/search";
        }
        model.addAttribute("person", person);
        model.addAttribute("civilites", Person.Civility.values());
        return "details";
    }

    /* ───── Modification (POST) avec validation Spring ───── */

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

        Person existing = personRepository.findById(id).orElse(null);
        if (existing == null) {
            return "redirect:/persons/search";
        }

        existing.setNom(person.getNom());
        existing.setPrenom(person.getPrenom());
        existing.setCivilite(person.getCivilite());
        existing.setAdresse(person.getAdresse());
        existing.setDateNaissance(person.getDateNaissance());

        personRepository.save(existing);

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

    /* ───── Ajout (POST) avec validation Spring ───── */

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

        personRepository.save(person);
        redirectAttributes.addFlashAttribute("success", "Personne ajoutée avec succès !");
        return "redirect:/persons/search";
    }

    /* ───── Suppression (POST) — ADMIN uniquement ───── */

    @PostMapping("/persons/{id}/delete")
    public String delete(
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes) {

        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Personne supprimée avec succès !");
        }
        return "redirect:/persons/search";
    }
}