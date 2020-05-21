/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.Controllers;

import com.example.TranslateService.DAO.Comment.CommentService;
import com.example.TranslateService.DAO.Document.DocumentService;
import com.example.TranslateService.DAO.Part.PartService;
import com.example.TranslateService.DAO.Person.PersonService;
import com.example.TranslateService.Entities.Comment;
import com.example.TranslateService.Entities.CommentData;
import com.example.TranslateService.Entities.Document;
import com.example.TranslateService.Entities.Part;
import com.example.TranslateService.Entities.Person;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Artur
 */
@Validated
@RestController
@CrossOrigin(origins = "*",allowCredentials = "true",allowedHeaders = "*",methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.PATCH,RequestMethod.DELETE,RequestMethod.OPTIONS})
public class CommentController {
    
    @Autowired
    private CommentService commentService;
    @Autowired
    private PersonService personService;
    @Autowired 
    private PartService partService;
    @Autowired
    private DocumentService documentService;
    
    @Transactional
    @MessageMapping("/document/{documentId}/comment")
    @SendTo("/topic/document/{documentId}/comment")
    public CommentData saveCommentByWebSocket(@Valid Comment comment,
            @DestinationVariable @Min(1) Long documentId,
            @AuthenticationPrincipal Person person){
        person=personService.findById(person.getId());
        Document document=documentService.findById(documentId);
        Part part=partService.findById(comment.getPart().getId());
        if (document==null && person==null && part==null && !documentService.containPart(part.getId(), documentId) && !documentService.hasAsses(documentId, person.getId()))
            throw new ValidationException("Bad request");
        comment.setPart(null);
        comment.setPerson(null);
        comment=commentService.save(comment);
        comment.setPerson(person);
        comment.setPart(part);
        comment=commentService.save(comment);
        return new CommentData(comment);
    }
    
}
