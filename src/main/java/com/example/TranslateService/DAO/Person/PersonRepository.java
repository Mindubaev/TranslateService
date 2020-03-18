/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.DAO.Person;

import com.example.TranslateService.Entities.Person;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Artur
 */
public interface PersonRepository extends JpaRepository<Person,Long>{
    
    Optional<Person> findByLoginAndPassword(String login,String password);
    Optional<Person> findByLogin(String login);
    @Modifying
    @Query(nativeQuery = true,value = "delete from person_project_relation where person_id=?1")
    void deleteProfileFromAllProjects(Long id);
    
}
