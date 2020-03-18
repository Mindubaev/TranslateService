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
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Artur
 */
@Validated
@RestController
public class TranslationController {
    
    @Autowired
    private TranslationService translationService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private PersonService personService;
    
    @Transactional
    @MessageMapping("/project/{projectId}/translation/queue")
    @SendTo("/topic/project/{projectId}/translation")
    public Translation saveTranslationByWebSocket(@Valid Translation translation,
            @DestinationVariable @Min(1) Long documentId,
            @AuthenticationPrincipal Person person){
        Project project=projectService.findById(documentId);
        if (project==null)
            throw new ValidationException("such project does not exist");
        person=personService.findById(person.getId());
        if (!hasAccesToProject(person, project))
            throw new ValidationException("access denied");
        translation.setProject(project);
        return translationService.save(translation);
    }
    
    @Transactional
    @GetMapping("/translation/{id}")
    public ResponseEntity<Translation> getTranslation(@PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person){
        person=personService.findById(person.getId());
        Translation translation=translationService.findById(id);
        if (translation==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        if (!hasAccesToProject(person, translation.getProject()))
           return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(translation,HttpStatus.FOUND);
    }
    
    @Transactional
    @GetMapping("/translation/{id}/project")
    public ResponseEntity<Project> getTranslationProject(@PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person){
        person=personService.findById(person.getId());
        Translation translation=translationService.findById(id);
        if (translation==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        if (!hasAccesToProject(person, translation.getProject()))
           return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(translation.getProject(),HttpStatus.FOUND);
    }
    
    private boolean hasAccesToProject(Person person,Project project) {
        return (person.getProjects().stream().filter(el->project.getId().equals(el.getId())).count()>0);
    }
    
    @Transactional
    @DeleteMapping("/translation/{id}")
    public ResponseEntity<Translation> deleteTranslation(@PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person){
        person=personService.findById(person.getId());
        Translation translation=translationService.findById(id);
        if (translation==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        if (!hasAccesToProject(person, translation.getProject()))
           return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        translationService.delete(translation);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PatchMapping("/translation/{id}")
    public ResponseEntity<Translation> patchTranslation(@PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person,
            @Valid Translation translation){
        person=personService.findById(person.getId());
        Translation translationFromBD=translationService.findById(id);
        if (translation==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        if (!hasAccesToProject(person, translationFromBD.getProject()))
           return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        translationFromBD.setOrigin(translation.getOrigin());
        translationFromBD.setTranslated(translation.getTranslated());
        translationFromBD=translationService.save(translationFromBD);
        return new ResponseEntity<>(translationFromBD,HttpStatus.FOUND);
    }
    
}
