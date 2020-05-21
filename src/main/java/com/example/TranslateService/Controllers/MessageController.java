/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.Controllers;

import com.example.TranslateService.DAO.Document.DocumentService;
import com.example.TranslateService.DAO.Message.MessageService;
import com.example.TranslateService.DAO.Person.PersonService;
import com.example.TranslateService.Entities.Document;
import com.example.TranslateService.Entities.Message;
import com.example.TranslateService.Entities.Person;
import com.example.TranslateService.Entities.Project;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Artur
 */
@Validated
@RestController
@CrossOrigin(origins = "*",allowCredentials = "true",allowedHeaders = "*",methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.PATCH,RequestMethod.DELETE,RequestMethod.OPTIONS})
public class MessageController {
    
    @Autowired
    private PersonService personService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private MessageService messageService;
    
    @Transactional
    @MessageMapping("/chat/{documentId}")
    @SendTo("/topic/chat/{documentId}")
    public Message messaging(@Valid Message message,
            @DestinationVariable @Min(1) Long documentId,
            @AuthenticationPrincipal Person person) throws ValidationException{
        Document document=documentService.findById(documentId);
        if (document==null)
            throw new ValidationException("such document doesn't exist");
        person=personService.findById(person.getId());
        if (!hasAccesToDoc(person, document))
            throw new AccessDeniedException("messaging to this topic is forbidden for "+person.getName()+"["+person.getId()+"]");
        message.setPerson(person);
        message.setDocument(document);
        return messageService.save(message);
    }
    
    @Transactional
    @GetMapping("/message/{id}")
    public ResponseEntity<Message> getMessage(@PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person){
        Message message=messageService.findById(id);
        if (message==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        Document document=message.getDocument();
        if (!hasAccesToDoc(person, document))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        return new ResponseEntity<Message>(message, HttpStatus.FOUND);
    }
    
    @Transactional
    @GetMapping("/message/{id}/profile")
    public ResponseEntity<Person> getMessageProfile(@PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person){
        Message message=messageService.findById(id);
        if (message==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        Document document=message.getDocument();
        if (!hasAccesToDoc(person, document))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        return new ResponseEntity<Person>(message.getPerson(), HttpStatus.FOUND);
    }
    
    @Transactional
    @GetMapping("/message/{id}/document")
    public ResponseEntity<Document> getMessageDocument(@PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person){
        Message message=messageService.findById(id);
        if (message==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        Document document=message.getDocument();
        if (!hasAccesToDoc(person, document))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        return new ResponseEntity<Document>(message.getDocument(), HttpStatus.FOUND);
    }
    
    @Transactional
    private boolean hasAccesToDoc(Person person, Document document) {
        person = personService.findById(person.getId());
        Project project=document.getProject();
        return (person.getProjects().stream().filter(el -> project.getId().equals(el.getId())).count() > 0 || person.getOwnProjects().stream().filter(el -> project.getId().equals(el.getId())).count() > 0);
    }
    
    @Transactional
    @DeleteMapping("/message/{id}")
    public ResponseEntity deleteMessage(@PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person){
        Message message=messageService.findById(id);
        if (message==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        if (!message.getPerson().getId().equals(person.getId()) || !message.getDocument().getProject().getPerson().getId().equals(person.getId()))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        messageService.delete(message);//можно сделать очередь для удаленых сообщений
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @PatchMapping("/message/{id}")
    public ResponseEntity<Message> patchMessage(@PathVariable @Min(1) Long id,
            @RequestBody @Valid Message message,
            @AuthenticationPrincipal Person person){
        Message messageFromDb=messageService.findById(id);
        if (messageFromDb==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        if (!messageFromDb.getPerson().getId().equals(person.getId()))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        messageFromDb.setText(message.getText());
        return new ResponseEntity<Message>(messageService.save(messageFromDb), HttpStatus.OK);
    }
    
}
