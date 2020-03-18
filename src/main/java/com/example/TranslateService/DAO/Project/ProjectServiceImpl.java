/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.DAO.Project;

import com.example.TranslateService.Entities.Person;
import com.example.TranslateService.Entities.Project;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Artur
 */
@Repository
@Service("projectService")
@Transactional
public class ProjectServiceImpl implements ProjectService{
    
    @Autowired
    private ProjectRepository projectRepository;

    public ProjectServiceImpl() {
    }

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project save(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public void delete(Project project) {
        projectRepository.delete(project);
    }
    
    private Project check(Optional<Project> optional){
        if (optional.isPresent())
            return optional.get();
        else
            return null;
    }

    private List<Project> checkList(Optional<List<Project>> optional){
        if (optional.isPresent())
            return optional.get();
        else
            return null;
    }
    
    private List<Project> checkPage(Optional<Page<Project>> optional){
        if (optional.isPresent())
            return optional.get().getContent();
        else
            return null;
    }
    
    @Transactional(readOnly = true)
    @Override
    public Project findById(Long id) {
        Optional<Project> optional=projectRepository.findById(id);
        return check(optional);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Project> findAll(Pageable pageable) {
        return projectRepository.findAll(pageable).getContent();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Project> findByPersonId(Long id) {
        Optional<List<Project>> optional=projectRepository.findByPersonId(id);
        return checkList(optional);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Project> findByPersonId(Long id, Pageable pageble) {
        Optional<Page<Project>> optional=projectRepository.findByPersonId(id, pageble);
        return checkPage(optional);
    }

    @Override
    public void deleteProjectFromAllPerson(Long id) {
        projectRepository.deleteProjectFromAllPerson(id);
    }
    
}
