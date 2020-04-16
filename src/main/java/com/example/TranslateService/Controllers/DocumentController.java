/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.Controllers;

import com.example.TranslateService.DAO.Document.DocumentService;
import com.example.TranslateService.DAO.History.HistoryService;
import com.example.TranslateService.DAO.Message.MessageService;
import com.example.TranslateService.DAO.Part.PartService;
import com.example.TranslateService.DAO.Person.PersonService;
import com.example.TranslateService.DAO.Project.ProjectService;
import com.example.TranslateService.Entities.Document;
import com.example.TranslateService.Entities.History;
import com.example.TranslateService.Entities.Message;
import com.example.TranslateService.Entities.Part;
import com.example.TranslateService.Entities.Person;
import com.example.TranslateService.Entities.Project;
import com.example.TranslateService.Utils.DocumentTextExtractor;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Artur
 */
@RestController
@Validated
public class DocumentController { 
   
    @Autowired
    private PersonService personService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private PartService partService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private MessageService MessageService;
    
    @PostMapping(value = "/document/marker")
    public ResponseEntity<Document> postDocument(@RequestParam("file") MultipartFile multipartFile,
            @RequestParam("projectId") @Min(1) Long projectId,
            @RequestParam("marker") String marker,
            @AuthenticationPrincipal Person person){
        Project project=projectService.findById(projectId);
        if (projectId==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        if (!person.getId().equals(project.getPerson().getId()))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        String[] parts=DocumentTextExtractor.extractTextFragmentatedByMarker(multipartFile, marker);
        return createDoc(multipartFile.getOriginalFilename(), project, parts);
    }
    
    @Transactional
    @PostMapping(value = "/document/expectingSize")
    public ResponseEntity<Document> postDocument(@RequestParam("file") MultipartFile multipartFile,
            @RequestParam("projectId") @Min(1) Long projectId,
            @RequestParam("expectingSize") int expectedSize,
            @AuthenticationPrincipal Person person){
        Project project=projectService.findById(projectId);
        if (projectId==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        if (!person.getId().equals(project.getPerson().getId()))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        String[] parts=DocumentTextExtractor.extractTextFragmentatedByExpectingSize(multipartFile, expectedSize);
        return createDoc(multipartFile.getOriginalFilename(), project, parts);
    }
    
    private ResponseEntity<Document> createDoc(String fileName,Project project,String[] parts){
        Document document=new Document();
        document.setName(fileName);
        document.setProject(project);
        document.setParts(new ArrayList<>());
        document=documentService.save(document);
        for (String origin:parts)
        {
            Part part=new Part(document, origin, "", new ArrayList<>());
            part=partService.save(part);
            document.getParts().add(part);
        }
        History history=new History(document, new ArrayList<>());
        history=historyService.save(history);
        document.setHistory(history);
        document=documentService.save(document);
        return new ResponseEntity<Document>(document,HttpStatus.CREATED);
    }
    
    @Transactional
    @GetMapping("/document/{id}")
    public ResponseEntity<Document> getDocumentInfo(@PathVariable @Min(1) Long id,
        @AuthenticationPrincipal Person person){
        Document document=documentService.findById(id);
        if (document==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        if (!hasAccesToDoc(person,document))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        return new ResponseEntity<Document>(document, HttpStatus.FOUND);
    }
    
    private boolean hasAccesToDoc(Person person,Document document) {
        person=personService.findById(person.getId());
        Project project=document.getProject();
        return (person.getProjects().stream().filter(el->project.getId().equals(el.getId())).count()>0);
    }
    
    @Transactional
    @GetMapping("/document/{id}/file/origin")
    public ResponseEntity<Resource> getDocumentFile(@PathVariable @Min(1) Long id,
        @AuthenticationPrincipal Person person){
        Document document=documentService.findById(id);
        if (document==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        if (!person.getId().equals(document.getProject().getPerson().getId()))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        Resource resource=documentService.assembleOriginFile(document);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + documentService.correctFileName(document.getName()) + "\"").body(resource);
    }
    
    @Transactional
    @GetMapping("/document/{id}/file/translated")
    public ResponseEntity<Resource> getDocumentTranslatedFile(@PathVariable @Min(1) Long id,
        @AuthenticationPrincipal Person person){
        Document document=documentService.findById(id);
        if (document==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        if (!person.getId().equals(document.getProject().getPerson().getId()))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        Resource resource=documentService.assembleTranslatedFile(document);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + documentService.correctFileName(document.getName()) + "\"").body(resource);
    }
    
    @GetMapping("/document/{id}/history")
    @Transactional
    public ResponseEntity<History> getDocumentHistory(@PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person){
        Document documentFromDB=documentService.findById(id);
        if (documentFromDB==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        if (!person.getId().equals(documentFromDB.getProject().getPerson().getId()))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        return new ResponseEntity<History>(documentFromDB.getHistory(), HttpStatus.FOUND);
    }
    
    @GetMapping("/document/{id}/project")
    @Transactional
    public ResponseEntity<Project> getDocumentProject(@PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person){
        Document documentFromDB=documentService.findById(id);
        if (documentFromDB==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        if (!person.getId().equals(documentFromDB.getProject().getPerson().getId()))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        return new ResponseEntity<Project>(documentFromDB.getProject(), HttpStatus.FOUND);
    }
    
    @GetMapping("/document/{id}/parts")
    @Transactional
    public ResponseEntity<List<Part>> getDocumentParts(@PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person){
        Document documentFromDB=documentService.findById(id);
        if (documentFromDB==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        if (!hasAccesToDoc(person, documentFromDB))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        return new ResponseEntity<List<Part>>(documentFromDB.getParts(), HttpStatus.FOUND);
    }
    
    @GetMapping("/document/{id}/parts/pageable")
    @Transactional
    public ResponseEntity<List<Part>> getDocumentParts(@PathVariable @Min(1) Long id,
            @RequestParam @Min(0) int page,
            @RequestParam @Min(0) int size,
            @AuthenticationPrincipal Person person){
        Document documentFromDB=documentService.findById(id);
        if (documentFromDB==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        if (!hasAccesToDoc(person, documentFromDB))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        return new ResponseEntity<List<Part>>(partService.findByDocumentIdOrderByIdAsc(id, PageRequest.of(page, size)), HttpStatus.FOUND);
    }
    
    @GetMapping("/document/{id}/messages")
    @Transactional
    public ResponseEntity<List<Message>> getDocumentMessages(@PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person){
        Document documentFromDB=documentService.findById(id);
        if (documentFromDB==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        if (!hasAccesToDoc(person, documentFromDB))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        return new ResponseEntity<List<Message>>(documentFromDB.getMessages(), HttpStatus.FOUND);
    }
    
    @GetMapping("/document/{id}/messages/pageable")
    @Transactional
    public ResponseEntity<List<Message>> getDocumentMessages(@PathVariable @Min(1) Long id,
            @RequestParam @Min(0) int page,
            @RequestParam @Min(0) int size,
            @AuthenticationPrincipal Person person){
        Document documentFromDB=documentService.findById(id);
        if (documentFromDB==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        if (!hasAccesToDoc(person, documentFromDB))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        return new ResponseEntity<List<Message>>(MessageService.findByDocumentIdOrderByDateAsc(id, PageRequest.of(page, size)), HttpStatus.FOUND);
    }
    
    @Transactional
    @PatchMapping("/document/{id}")
    public ResponseEntity<Document> patchDocument(@RequestBody @Valid Document document,
            @PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person){
        Document documentFromDB=documentService.findById(id);
        if (documentFromDB==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        if (!person.getId().equals(documentFromDB.getProject().getPerson().getId()))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        documentFromDB.setName(document.getName());
        documentFromDB=documentService.save(documentFromDB);
        return new ResponseEntity<Document>(documentFromDB, HttpStatus.OK);
    }
    
    @Transactional
    @DeleteMapping("/document/{id}")
    public ResponseEntity deleteDocument(@PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person){
        Document documentFromDB=documentService.findById(id);
        if (documentFromDB==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        if (!person.getId().equals(documentFromDB.getProject().getPerson().getId()))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        documentService.delete(documentFromDB);
        return new ResponseEntity(HttpStatus.OK);
    }
    
}
