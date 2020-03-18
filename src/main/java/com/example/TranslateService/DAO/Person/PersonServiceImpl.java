/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.DAO.Person;

import com.example.TranslateService.Entities.Person;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Artur
 */
@Repository
@Service("personService")
@Transactional
public class PersonServiceImpl implements PersonService{

    @Autowired
    private PersonRepository personRepository;
    
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PersonServiceImpl() {
    }
    
    @Override
    public Person save(Person person) {
        return personRepository.save(person);
        
    }

    @Override
    public void delete(Person person) {
        personRepository.delete(person);
    }

    @Transactional(readOnly = true)
    @Override
    public Person findById(Long id) {
        Optional<Person> optional=personRepository.findById(id);
        if (optional.isPresent())
            return optional.get();
        else
            return null;
    }

    @Transactional(readOnly = true)
    @Override
    public Person findByLoginAndPassword(String login,String password) {
        Optional<Person> optional=personRepository.findByLoginAndPassword(login, password);
        if (optional.isPresent())
            return optional.get();
        else
            return null;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Person> findAll(Pageable pageable) {
        return personRepository.findAll(pageable).getContent();
    }

    @Transactional(readOnly = true)
    @Override
    public Person findByLogin(String login){
        Optional<Person> optional = personRepository.findByLogin(login);
        if (optional.isPresent())
            return optional.get();
        else
            return null;
    }

    @Override
    public void deleteById(Long id) {
        personRepository.deleteById(id);
    }
    
    @Override
    public void deleteProfileFromAllProjects(Long id){
        personRepository.deleteProfileFromAllProjects(id);
    }
    
}
