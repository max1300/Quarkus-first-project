package org.acme.services;

import org.acme.models.Person;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@ApplicationScoped
@Transactional(Transactional.TxType.REQUIRED)
public class PersonService {

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Person> findAllPerson(){
        return Person.listAll();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public Person findPersonById(Long id){
        return Person.findById(id);
    }

    @Transactional
    public Person persistPerson(@Valid Person person){
        Person.persist(person);
        return person;
    }

    public Person updatePerson(@Valid Person person){
        Person entity = Person.findById(person.id);
        entity.name = person.name;
        entity.login = person.login;
        entity.password = person.password;
        entity.email = person.email;
        return entity;
    }

    public void deletePerson(Long id){
        Person deletePerson = Person.findById(id);
        deletePerson.delete();
    }

}
