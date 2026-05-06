package com.company.person.service;

import com.company.person.model.Person;
import com.company.person.repository.PersonRepository;

public class PersonService {

    private final PersonRepository repository;

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    public void createPerson(Person person) throws Exception {
        repository.insert(person);
    }

    public int countPersons() throws Exception {
        return repository.count();
    }

    public void clearPersons() throws Exception {
        repository.deleteAll();
    }
}
