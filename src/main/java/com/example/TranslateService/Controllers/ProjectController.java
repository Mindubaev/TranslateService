/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.Controllers;

import com.example.TranslateService.DAO.Person.PersonService;
import com.example.TranslateService.DAO.Project.ProjectService;
import com.example.TranslateService.DAO.Translation.TranslationService;
import com.example.TranslateService.Entities.Document;
import com.example.TranslateService.Entities.Person;
import com.example.TranslateService.Entities.Project;
import com.example.TranslateService.Entities.Translation;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Artur
 */
@RestController
@Validated
public class ProjectController {
    
    @Autowired
    private PersonService personService;
    @Autowired
    private ProjectService projectService;
    
    @PostMapping("/project")
    public ResponseEntity<Project> postProject(@RequestBody Project project,
            @AuthenticationPrincipal Person person){
        person=personService.findById(person.getId());
        project.setPerson(person);
        project.setPersons(List.of(person));
        project.setTranslations(new ArrayList<>());
        project.setDocuments(new ArrayList<>());
        return new ResponseEntity<>(projectService.save(project),HttpStatus.CREATED);
    }
    
    @GetMapping("/project/{id}")
    public ResponseEntity<Project> getProject(@PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person){
        if (hasAccesToGetProjectInfo(person, id)){
            Project project=projectService.findById(id);
            if (project!=null)
                return new ResponseEntity<Project>(project,HttpStatus.FOUND);
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    
    @Transactional
    private boolean hasAccesToGetProjectInfo(Person person,Long projectId){
        person=personService.findById(person.getId());
        return person.getProjects().stream().filter(el->projectId.equals(el.getId())).count()>0;
    }
    
    @Transactional
    @GetMapping("/project/{id}/owner")
    public ResponseEntity<Person> getOwner(@PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person){
        if (hasAccesToGetProjectInfo(person, id)){
            Project project=projectService.findById(id);
            if (project!=null)
                return new ResponseEntity<>(project.getPerson(),HttpStatus.FOUND);
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    
    @Transactional
    @GetMapping("/project/{id}/persons")
    public ResponseEntity<List<Person>> getProjectPersons(@PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person){
        if (hasAccesToGetProjectInfo(person, id)){
            Project project=projectService.findById(id);
            if (project!=null)
                return new ResponseEntity<>(project.getPersons(),HttpStatus.FOUND);
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    
    @Transactional
    @GetMapping("/project/{id}/persons/pageable")
    public ResponseEntity<List<Person>> getProjectPersons(@PathVariable @Min(1) Long id,
            @RequestParam @Min(0) int page,
            @RequestParam @Min(0) int size,
            @AuthenticationPrincipal Person person){
        if (hasAccesToGetProjectInfo(person, id)){
            Project project=projectService.findById(id);
            if (project!=null)
                return new ResponseEntity<>(getCurrentPageFromList(page, size, project.getPersons()),HttpStatus.FOUND);
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    
    @Transactional
    @GetMapping("/project/{id}/translations")
    public ResponseEntity<List<Translation>> getProjectTranslations(@PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person){
        if (hasAccesToGetProjectInfo(person, id)){
            Project project=projectService.findById(id);
            if (project!=null)
                return new ResponseEntity<>(project.getTranslations(),HttpStatus.FOUND);
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    
    @Transactional
    @GetMapping("/project/{id}/translations/pageable")
    public ResponseEntity<List<Translation>> getProjectTranslations(@PathVariable @Min(1) Long id,
            @RequestParam @Min(0) int page,
            @RequestParam @Min(0) int size,
            @AuthenticationPrincipal Person person){
        if (hasAccesToGetProjectInfo(person, id)){
            Project project=projectService.findById(id);
            if (project!=null)
                return new ResponseEntity<>(getCurrentPageFromList(page, size, project.getTranslations()),HttpStatus.FOUND);
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    
    @Transactional
    @GetMapping("/project/{id}/documents")
    public ResponseEntity<List<Document>> getProjectDocuments(@PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person){
        if (hasAccesToGetProjectInfo(person, id)){
            Project project=projectService.findById(id);
            if (project!=null)
                return new ResponseEntity<>(project.getDocuments(),HttpStatus.FOUND);
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    
    @Transactional
    @GetMapping("/project/{id}/documents/pageable")
    public ResponseEntity<List<Document>> getProjectDocuments(@PathVariable @Min(1) Long id,
            @RequestParam @Min(0) int page,
            @RequestParam @Min(0) int size,
            @AuthenticationPrincipal Person person){
        if (hasAccesToGetProjectInfo(person, id)){
            Project project=projectService.findById(id);
            if (project!=null)
                return new ResponseEntity<>(getCurrentPageFromList(page, size, project.getDocuments()),HttpStatus.FOUND);
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    
    @Transactional
    @PatchMapping("/project")
    public ResponseEntity<Project> patchProject(@RequestBody Project project){
        if (project.getId()==null || (project.getId()!=null && project.getId()<1))
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        Project projectFromDB=projectService.findById(project.getId());
        if (projectFromDB==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        if (!projectFromDB.getPerson().getId().equals(projectFromDB))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        projectFromDB.setName(project.getName());
        projectFromDB=projectService.save(projectFromDB);
        return new ResponseEntity<Project>(projectFromDB,HttpStatus.OK);
    }       
    
    @Transactional
    @DeleteMapping("/project/{id}")
    public ResponseEntity deleteProject(@PathVariable @Min(1) Long id){
        Project project=projectService.findById(id);
        if (project==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        if (!id.equals(project.getPerson().getId()))
            return new  ResponseEntity(HttpStatus.FORBIDDEN);
        projectService.deleteProjectFromAllPerson(id);
        projectService.delete(project);
        return new ResponseEntity(HttpStatus.OK);
    }
    
    
    private List getCurrentPageFromList(int page,int size,List list){
        int listSize = list.size();
            int start = page * size;
            if (start >= listSize) {
                return new ArrayList();
            }
            int finish = start + size;
            if (finish >= listSize) {
                finish = listSize;
            }
            return list.subList(start, finish);
    } 
    
}
