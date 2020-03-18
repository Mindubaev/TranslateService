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

/**
 *
 * @author Artur
 */
public interface ProjectService {
    
    Project save(Project project);
    void delete(Project project);
    Project findById(Long id);
    List<Project> findAll();
    List<Project> findAll(Pageable pageable);
    List<Project> findByPersonId(Long id);
    List<Project> findByPersonId(Long id,Pageable pageble);
    void deleteProjectFromAllPerson(Long id);
    
}
