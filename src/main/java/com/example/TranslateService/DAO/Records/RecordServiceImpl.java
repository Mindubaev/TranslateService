/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.DAO.Records;

import com.example.TranslateService.Entities.Record;
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
@Service("recordService")
@Transactional
public class RecordServiceImpl implements RecordService{
    
    @Autowired
    private RecordRepository recordRepository;

    public RecordServiceImpl(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Override
    public Record save(Record record) {
        return recordRepository.save(record);
    }

    @Override
    public void delete(Record record) {
        recordRepository.delete(record);
    }

    @Transactional(readOnly = true)
    @Override
    public Record findById(Long id) {
        Optional<Record> optional= recordRepository.findById(id);
        if (optional.isPresent())
            return optional.get();
        else
            return null;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Record> findAll() {
        return recordRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Record> findAll(Pageable pageable) {
        return recordRepository.findAll(pageable).getContent();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Record> findByHistoryIdOrderByDateAsc(Long id) {
        return checkList(recordRepository.findByHistoryIdOrderByDateAsc(id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Record> findByHistoryIdOrderByDateAsc(Long id, Pageable pageable) {
        return checkPage(recordRepository.findByHistoryIdOrderByDateAsc(id, pageable));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Record> findByPersonIdOrderByDateAsc(Long id) {
        return checkList(recordRepository.findByPersonIdOrderByDateAsc(id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Record> findByPersonIdOrderByDateAsc(Long id, Pageable pageable) {
        return checkPage(recordRepository.findByPersonIdOrderByDateAsc(id,pageable));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Record> findByDocumentIdAndPersonIdOrderByDateAsc(Long documentId, Long personId) {
        return checkList(recordRepository.findByHistoryIdAndPersonIdOrderByDateAsc(personId, personId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Record> findByDocumentIdAndPersonIdOrderByDateAsc(Long documentId, Long personId, Pageable pageable) {
        return checkPage(recordRepository.findByHistoryIdAndPersonIdOrderByDateAsc(personId, personId,pageable));
    }
    
    private List<Record> checkList(Optional<List<Record>> optional) {
        if (optional.isPresent()) {
            return optional.get();
        } else {
            return null;
        }
    }

    private List<Record> checkPage(Optional<Page<Record>> optional) {
        if (optional.isPresent()) {
            return optional.get().getContent();
        } else {
            return null;
        }
    }

    @Override
    public List<Record> findByPartIdOrderByDateAsc(Long partId) {
        return checkList(recordRepository.findByPartIdOrderByDateAsc(partId));
    }

    @Override
    public List<Record> findByPartIdOrderByDateAsc(Long partId, Pageable pageable) {
        return checkPage(recordRepository.findByPartIdOrderByDateAsc(partId, pageable));
    }
    
}
