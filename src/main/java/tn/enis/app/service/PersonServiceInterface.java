package tn.enis.app.service;

import tn.enis.app.entities.Person;

import java.util.List;

public interface PersonServiceInterface {
    public List<Person> findAll();
    public List<Person> search(String nom, String prenom, Person.Civility civilite);
    public Person findById(Long id);
    public Person save(Person person);
    public Person update(Long id, Person person);
    public boolean deleteById(Long id);

}
