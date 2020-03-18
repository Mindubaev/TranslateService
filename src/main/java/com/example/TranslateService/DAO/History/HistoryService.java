/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.DAO.History;

import com.example.TranslateService.Entities.History;
import com.example.TranslateService.Entities.Message;
import java.util.List;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Artur
 */
public interface HistoryService {
    
    History save(History history);
    void delete(History History);
    History findById(Long id);
    History findByDocumentId(Long id); 
    List<History> findAll();
    List<History> findAll(Pageable pageable);
    
}
