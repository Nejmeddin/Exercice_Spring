    package tn.enis.app.repository;

    import org.springframework.data.jpa.domain.Specification;
    import tn.enis.app.entities.Person;

    public class PersonSpecification {

        public static Specification<Person> nomContains(String nom) {
            return (root, query, cb) ->
                    cb.like(cb.lower(root.get("nom")), "%" + nom.toLowerCase() + "%");
        }

        public static Specification<Person> prenomContains(String prenom) {
            return (root, query, cb) ->
                    cb.like(cb.lower(root.get("prenom")), "%" + prenom.toLowerCase() + "%");
        }

        public static Specification<Person> civiliteEquals(Person.Civility civilite) {
            return (root, query, cb) ->
                    cb.equal(root.get("civilite"), civilite);
        }
    }

