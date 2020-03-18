/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.DAO.Project;

import com.example.TranslateService.Entities.Project;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Artur
 */
public interface ProjectRepository extends JpaRepository<Project, Long>{
    
    Optional<List<Project>> findByPersonId(Long id);
    Optional<Page<Project>> findByPersonId(Long id,Pageable pageble);
    @Modifying
    @Query(nativeQuery = true,value = "delete from person_project_relation where project_id=?1")
    void deleteProjectFromAllPerson(Long id);
    
}
