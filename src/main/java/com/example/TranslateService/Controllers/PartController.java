/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.Controllers;

import com.example.TranslateService.DAO.Document.DocumentService;
import com.example.TranslateService.DAO.Part.PartService;
import com.example.TranslateService.DAO.Person.PersonService;
import com.example.TranslateService.DAO.Project.ProjectService;
import com.example.TranslateService.DAO.Records.RecordService;
import com.example.TranslateService.Entities.Document;
import com.example.TranslateService.Entities.Part;
import com.example.TranslateService.Entities.Person;
import com.example.TranslateService.Entities.Project;
import com.example.TranslateService.Entities.Record;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Artur
 */
@Validated
@RestController
public class PartController {
    
    @Autowired
    private PersonService personService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private PartService partService;
    @Autowired
    private RecordService recordService;
    
    @Transactional
    @MessageMapping("/document/{documentId}/part/queue")
    @SendTo("/topic/document/{documentId}/part")
    public Part patchPartByWebSocket(@Valid Part part,
            @DestinationVariable @Min(1) Long documentId,
            @AuthenticationPrincipal Person person){
        Part partFromDB=partService.findById(part.getId());
        Document document=documentService.findById(documentId);
        if (document==null)
          throw new ValidationException("such document does not exist");  
        person=personService.findById(person.getId());
        if (!hasAccesToProject(person, document.getProject()))
           throw new ValidationException("access denied");
        Record record=new Record(document.getHistory(), partFromDB, person,new Date() , part.getTranslated());
        recordService.save(record);
        partFromDB.setTranslated(part.getTranslated());
        return partService.save(partFromDB);
    }
    
    private boolean hasAccesToProject(Person person,Project project) {
        return (person.getProjects().stream().filter(el->project.getId().equals(el.getId())).count()>0);
    }
    
    @Transactional
    @GetMapping("/part/{id}")
    public ResponseEntity<Part> getPart(@PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person){
        Part part=partService.findById(id);
        if (part==null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        person=personService.findById(person.getId());
        if (!hasAccesToProject(person, part.getDocument().getProject()))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(part,HttpStatus.FOUND);
    } 
    
    @Transactional
    @GetMapping("/part/{id}/document")
    public ResponseEntity<Document> getPartDocument(@PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person){
         Part part=partService.findById(id);
        if (part==null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        person=personService.findById(person.getId());
        if (!hasAccesToProject(person, part.getDocument().getProject()))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        ResponseEntity e=new ResponseEntity(HttpStatus.CREATED);
        return new ResponseEntity<>(part.getDocument(),HttpStatus.FOUND);
    }
    
    @Transactional
    @GetMapping("/part/{id}/records")
    public ResponseEntity<List<Record>> getPartRecords(@PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person){
         Part part=partService.findById(id);
        if (part==null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        person=personService.findById(person.getId());
        if (!hasAccesToProject(person, part.getDocument().getProject()))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        ResponseEntity e=new ResponseEntity(HttpStatus.CREATED);
        return new ResponseEntity<>(part.getRecords(),HttpStatus.FOUND);
    }
    
    @Transactional
    @GetMapping("/part/{id}/records/pageable")
    public ResponseEntity<List<Record>> getPartRecords(@PathVariable @Min(1) Long id,
            @RequestParam @Min(0) int page,
            @RequestParam @Min(0) int size,
            @AuthenticationPrincipal Person person){
        Part part=partService.findById(id);
        if (part==null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        person=personService.findById(person.getId());
        if (!hasAccesToProject(person, part.getDocument().getProject()))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        ResponseEntity e=new ResponseEntity(HttpStatus.CREATED);
        return new ResponseEntity<>(recordService.findByPartIdOrderByDateAsc(id, PageRequest.of(page, size)),HttpStatus.FOUND);
    }
    
    @Transactional
    @PatchMapping("/part/{id}")
    public ResponseEntity<Part> patchPart(@PathVariable @Min(1) Long id,
            @RequestBody @Valid Part part,
            @AuthenticationPrincipal Person person){
        Part partFromDB=partService.findById(id);
        if (partFromDB==null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        person=personService.findById(person.getId());
        if (!isOwner(person, partFromDB))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        partFromDB.setOrigin(part.getOrigin());
        partFromDB.setTranslated(part.getTranslated());
        return new ResponseEntity<>(partService.save(partFromDB),HttpStatus.OK);
    }
    
    @Transactional
    @DeleteMapping("/part/{id}")
    public ResponseEntity deletePart(@PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person){
        Part partFromDB=partService.findById(id);
        if (partFromDB==null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        person=personService.findById(person.getId());
        if (!isOwner(person, partFromDB))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        partService.delete(partFromDB);
        return new ResponseEntity(HttpStatus.OK);
    }
    
    private boolean isOwner(Person person,Part part){
        return person.getId().equals(part.getDocument().getProject().getPerson().getId());
    }
    
}
