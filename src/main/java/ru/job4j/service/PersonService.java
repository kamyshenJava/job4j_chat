package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.domain.Person;
import ru.job4j.domain.Role;
import ru.job4j.repository.PersonRepository;

import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final RoleService roleService;

    public PersonService(PersonRepository personRepository, RoleService roleService) {
        this.personRepository = personRepository;
        this.roleService = roleService;
    }

    public Optional<Person> add(Person person) {
        person.setRole(Role.of(1, "ROLE_USER"));
        person.setEnabled(true);
        try {
            return Optional.of(personRepository.save(person));
        } catch (Exception e) {
            return Optional.empty();
        }

    }

    public Optional<Person> findPersonByUsernameAndPassword(String name, String password) {
        return personRepository.findPersonByUsernameAndPassword(name, password);
    }
}
