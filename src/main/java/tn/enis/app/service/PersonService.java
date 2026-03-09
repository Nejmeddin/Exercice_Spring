package tn.enis.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tn.enis.app.entities.Person;
import tn.enis.app.repository.PersonRepository;
import tn.enis.app.repository.PersonSpecification;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService implements PersonServiceInterface {

    private final PersonRepository personRepository;

    @Override
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @Override
    public List<Person> search(String nom, String prenom, Person.Civility civilite) {
        boolean hasNom = nom != null && !nom.isBlank();
        boolean hasPrenom = prenom != null && !prenom.isBlank();
        boolean hasCivilite = civilite != null;

        if (!hasNom && !hasPrenom && !hasCivilite) {
            return Collections.emptyList();
        }

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

        return personRepository.findAll(spec);
    }

    @Override
    public Person findById(Long id) {
        return personRepository.findById(id).orElse(null);
    }

    @Override
    public Person save(Person person) {
        return personRepository.save(person);
    }

    @Override
    public Person update(Long id, Person person) {
        Person existing = personRepository.findById(id).orElse(null);

        if (existing == null) {
            return null;
        }

        existing.setNom(person.getNom());
        existing.setPrenom(person.getPrenom());
        existing.setCivilite(person.getCivilite());
        existing.setAdresse(person.getAdresse());
        existing.setDateNaissance(person.getDateNaissance());

        return personRepository.save(existing);
    }
    @Override
    public boolean deleteById(Long id) {
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
            return true;
        }
        return false;
    }
}