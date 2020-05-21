/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.DAO.Document;

import com.example.TranslateService.Entities.Document;
import com.example.TranslateService.Entities.Part;
import com.example.TranslateService.Entities.Record;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Artur
 */
public interface DocumentService {
    
    Document save(Document document);
    void delete(Document document);
    Document findById(Long id);
    List<Document> findAll();
    List<Document> findAll(Pageable pageable);
    List<Document> findByProjectIdOrderByIdAsc(Long id); 
    List<Document> findByProjectIdOrderByIdAsc(Long id,Pageable pageable);
    boolean containPart(Long partId,Long documentId);
    boolean hasAsses(Long documentId,Long personId);
    List<Record> findRecordsByDocumentId(Long documentId);
    List<Record> findRecordsByDocumentId(Long documentId,int size,int page);
    
    default Resource assembleOriginFile(Document document){
        String content="";
        Collections.sort(document.getParts(), (el1, el2) -> el1.getId().compareTo(el2.getId()));
        for (Part part:document.getParts())
            if (part.getOrigin()!=null)
            content=content+part.getOrigin();
        Resource resource=new ByteArrayResource(content.getBytes(), correctFileName(document.getName()));
        return resource;
    }
    
    default Resource assembleTranslatedFile(Document document){
        String content="";
        Collections.sort(document.getParts(), (el1, el2) -> el1.getId().compareTo(el2.getId()));
        for (Part part:document.getParts())
            if (part.getOrigin()!=null)
            content=content+part.getTranslated();
        Resource resource=new ByteArrayResource(content.getBytes(), correctFileName(document.getName()));
        return resource;
    }
    
    default String correctFileName(String fileName){
        int dotIndex=fileName.lastIndexOf(".");
        return fileName.substring(0,dotIndex+1)+"txt";    
    }
    
}
