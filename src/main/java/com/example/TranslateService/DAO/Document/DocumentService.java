/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.DAO.Document;

import com.example.TranslateService.Entities.Document;
import java.util.List;
import java.util.Optional;
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
    
}
