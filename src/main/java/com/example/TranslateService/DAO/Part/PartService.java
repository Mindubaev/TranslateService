/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.DAO.Part;

import com.example.TranslateService.Entities.Part;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Artur
 */
public interface PartService {
    
    Part save(Part part);
    void delete(Part part);
    Part findById(Long id);
    List<Part> findAll();
    List<Part> findAll(Pageable pageable);
    List<Part> findByDocumentIdOrderByIdAsc(Long documentId);
    List<Part> findByDocumentIdOrderByIdAsc(Long documentId,Pageable pageable);
    
}
