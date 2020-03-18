/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.DAO.Records;

import com.example.TranslateService.Entities.Record;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Artur
 */
public interface RecordService {
    
    Record save(Record record);
    void delete(Record record);
    Record findById(Long id);
    List<Record> findAll();
    List<Record> findAll(Pageable pageable);
    List<Record> findByHistoryIdOrderByDateAsc(Long id);
    List<Record> findByHistoryIdOrderByDateAsc(Long id,Pageable pageable);
    List<Record> findByPersonIdOrderByDateAsc(Long id);
    List<Record> findByPersonIdOrderByDateAsc(Long id,Pageable pageable);
    List<Record> findByDocumentIdAndPersonIdOrderByDateAsc(Long documentId,Long personId);
    List<Record> findByDocumentIdAndPersonIdOrderByDateAsc(Long documentId,Long personId,Pageable pageable);
    List<Record> findByPartIdOrderByDateAsc(Long partId);
    List<Record> findByPartIdOrderByDateAsc(Long partId,Pageable pageable);
}
