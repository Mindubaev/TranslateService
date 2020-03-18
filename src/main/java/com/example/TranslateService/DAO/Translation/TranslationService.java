/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.DAO.Translation;

import com.example.TranslateService.Entities.Translation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Artur
 */
public interface TranslationService {
    
    Translation save(Translation translation);
    void delete(Translation translation);
    Translation findById(Long id);
    List<Translation> findAll();
    List<Translation> findAll(Pageable pageable);
    List<Translation> findByProjectIdOrderByIdAsc(Long projectId);
    List<Translation> findByProjectIdOrderByIdAsc(Long projectId,Pageable pageable);
    
}
