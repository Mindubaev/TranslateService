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

/**
 *
 * @author Artur
 */
public interface MessageService {
    
    Message save(Message message);
    void delete(Message message);
    Message findById(Long id);
    List<Message> findAll();
    List<Message> findAll(Pageable pageable);
    List<Message> findByDocumentIdOrderByDateAsc(Long id);
    List<Message> findByDocumentIdOrderByDateAsc(Long id,Pageable pageable);
    List<Message> findByPersonIdOrderByDateAsc(Long id);
    List<Message> findByPersonIdOrderByDateAsc(Long id,Pageable pageable);
    
}
