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
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Artur
 */
public interface TranslationRepository extends JpaRepository<Translation, Long>{
    
    Optional<List<Translation>> findByProjectIdOrderByIdAsc(Long projectId);
    Optional<Page<Translation>> findByProjectIdOrderByIdAsc(Long projectId,Pageable pageable);
    
}
