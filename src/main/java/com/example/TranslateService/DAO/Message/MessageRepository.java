/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.DAO.Message;

import com.example.TranslateService.Entities.Message;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Artur
 */
public interface MessageRepository extends JpaRepository<Message, Long>{
    
    Optional<List<Message>> findByDocumentIdOrderByDateAsc(Long id);
    Optional<Page<Message>> findByDocumentIdOrderByDateAsc(Long id,Pageable pageable);
    Optional<List<Message>> findByPersonIdOrderByDateAsc(Long id);
    Optional<Page<Message>> findByPersonIdOrderByDateAsc(Long id,Pageable pageable);
    
}
