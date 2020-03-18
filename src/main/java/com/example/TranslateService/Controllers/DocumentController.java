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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
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
        String text =extractText(multipartFile);
        String[] parts=textFragmentationByMarker(text, marker);
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
        String text =extractText(multipartFile);
        String[] parts=textFragmentationByPartSize(text, expectedSize);
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
    
    public static String extractText(MultipartFile multipartFile){
        try(InputStream inputStream=multipartFile.getInputStream()){
            String contentType=multipartFile.getContentType();
            switch(contentType){
                case "text/plain":
                    return new String(inputStream.readAllBytes());
                case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                    XWPFDocument document=new XWPFDocument(inputStream);
                    XWPFWordExtractor extractor=new XWPFWordExtractor(document);
                    return extractor.getText();
                case "application/msword":
                    WordExtractor wd = new WordExtractor(inputStream);
                    return wd.getText();
                default:
                    return null;
            }
        }catch(IOException ex){
            return null;
        }
    }
    
    public static String[] textFragmentationByMarker(String text,String marker){
        String[] parts=text.split(Pattern.quote(marker));
        Stream<String> stream=Arrays.stream(parts).filter(e->!e.equals(""));
        return stream.toArray(size->new String[size]);
    }
    
    public static String[] textFragmentationByPartSize(String text,int expectingSize){
        int nextIndex=0;
        int prevIndex=0;
        List<String> parts=new ArrayList<String>();
        while (nextIndex<text.length()-1){
            prevIndex=nextIndex;
            if (prevIndex>0 && prevIndex<text.length()-1)
                prevIndex++;
            nextIndex+=expectingSize;
            if (nextIndex>text.length()-1)
                nextIndex=text.length()-1;
            nextIndex=findFirstClosestPunctuationchar(text, nextIndex);
            parts.add(text.substring(prevIndex, nextIndex+1));
        }
        return parts.toArray(size->new String[size]);
    }
    
    private static boolean isEndOfSentence(char c){
       return (c=='.' || c==',' || c=='!' || c=='?');
    }
    
    private static int checkMultiCharPunctuation(String text,int startPosition){
        while (text.length()-1>startPosition && isEndOfSentence(text.charAt(startPosition)))
            startPosition++;
        return startPosition-1;
    }
    
    private static int findFirstClosestPunctuationchar(String text,int startPosition){
        while (text.length()-1>startPosition && !isEndOfSentence(text.charAt(startPosition)))
            startPosition++;
        if (text.length()-1==startPosition)
            return startPosition;
        else
            return checkMultiCharPunctuation(text, startPosition);
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
        Resource resource=assembleOriginFile(document);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + correctFileName(document.getName()) + "\"").body(resource);
    }
    
    private Resource assembleOriginFile(Document document){
        String content="";
        Collections.sort(document.getParts(), (el1, el2) -> el1.getId().compareTo(el2.getId()));
        for (Part part:document.getParts())
            if (part.getOrigin()!=null)
            content=content+part.getOrigin();
        Resource resource=new ByteArrayResource(content.getBytes(), correctFileName(document.getName()));
        return resource;
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
        Resource resource=assembleTranslatedFile(document);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + correctFileName(document.getName()) + "\"").body(resource);
    }
    
    private Resource assembleTranslatedFile(Document document){
        String content="";
        Collections.sort(document.getParts(), (el1, el2) -> el1.getId().compareTo(el2.getId()));
        for (Part part:document.getParts())
            if (part.getOrigin()!=null)
            content=content+part.getTranslated();
        Resource resource=new ByteArrayResource(content.getBytes(), correctFileName(document.getName()));
        return resource;
    }
    
    private String correctFileName(String fileName){
        int dotIndex=fileName.lastIndexOf(".");
        return fileName.substring(0,dotIndex+1)+"txt";    
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
