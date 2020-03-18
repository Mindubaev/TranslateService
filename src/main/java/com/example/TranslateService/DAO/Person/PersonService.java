/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.DAO.Person;

import com.example.TranslateService.Entities.Person;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Artur
 */
public interface PersonService {
    
    Person save(Person person);
    void delete(Person person);
    void deleteById(Long id);
    Person findById(Long id);
    Person findByLogin(String login);
    Person findByLoginAndPassword(String login,String password);
    List<Person> findAll();
    List<Person> findAll(Pageable pageable);
    void deleteProfileFromAllProjects(Long id);
    
}
