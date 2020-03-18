/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.DAO.Records;

import com.example.TranslateService.Entities.Record;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Artur
 */
public interface RecordRepository extends JpaRepository<Record, Long>{
    
    Optional<List<Record>> findByPartIdOrderByDateAsc(Long partId);
    Optional<Page<Record>> findByPartIdOrderByDateAsc(Long partId,Pageable pageable);
    Optional<List<Record>> findByHistoryIdOrderByDateAsc(Long historyId);
    Optional<Page<Record>> findByHistoryIdOrderByDateAsc(Long historyId,Pageable pageable);
    Optional<List<Record>> findByPersonIdOrderByDateAsc(Long personId);
    Optional<Page<Record>> findByPersonIdOrderByDateAsc(Long personId,Pageable pageable);
    Optional<List<Record>> findByHistoryIdAndPersonIdOrderByDateAsc(Long historyId,Long personId);
    Optional<Page<Record>> findByHistoryIdAndPersonIdOrderByDateAsc(Long historyId,Long personId,Pageable pageable);
    
}
