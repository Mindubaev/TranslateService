/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.DAO.History;

import com.example.TranslateService.Entities.History;
import com.example.TranslateService.Entities.Message;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Artur
 */
@Repository
@Service("historyService")
@Transactional
public class HistoryServiceImpl implements HistoryService{
    
    @Autowired
    private HistoryRepository historyRepository;

    public HistoryServiceImpl(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public History save(History history) {
        return historyRepository.save(history);
    }

    @Override
    public void delete(History History) {
        historyRepository.delete(History);
    }

    @Transactional(readOnly = true)
    @Override
    public History findById(Long id) {
        return checkOptional(historyRepository.findById(id));
    }

    @Transactional(readOnly = true)
    @Override
    public History findByDocumentId(Long id) {
        return checkOptional(historyRepository.findByDocumentId(id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<History> findAll() {
        return historyRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<History> findAll(Pageable pageable) {
        return historyRepository.findAll(pageable).getContent();
    }
    
    private List<History> checkList(Optional<List<History>> optional) {
        if (optional.isPresent())
            return optional.get();
        else
            return null;
    }

    private List<History> checkPage(Optional<Page<History>> optional) {
        if (optional.isPresent()) 
            return optional.get().getContent();
        else 
            return null;
    }
    
    private History checkOptional(Optional<History> optional){
        if (optional.isPresent())
            return optional.get();
        else 
            return null;
    }
    
}
