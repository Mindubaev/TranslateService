/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.DAO.Document;

import com.example.TranslateService.Entities.Document;
import com.example.TranslateService.Entities.Project;
import com.example.TranslateService.Entities.Record;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Artur
 */
@Transactional
@Repository
@Service("documentService")
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public DocumentServiceImpl() {
    }

    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public Document save(Document document) {
        return documentRepository.save(document);
    }

    @Override
    public void delete(Document document) {
        documentRepository.delete(document);
    }

    @Transactional(readOnly = true)
    @Override
    public Document findById(Long id) {
        return check(documentRepository.findById(id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Document> findAll(Pageable pageable) {
        return documentRepository.findAll(pageable).getContent();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Document> findByProjectIdOrderByIdAsc(Long id) {
        return checkList(documentRepository.findByProjectIdOrderByIdAsc(id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Document> findByProjectIdOrderByIdAsc(Long id, Pageable pageable) {
        return checkPage(documentRepository.findByProjectIdOrderByIdAsc(id, pageable));
    }
    
    @Override
    public List<Record> findRecordsByDocumentId(Long documentId) {
        return documentRepository.findRecordsByDocumentId(documentId);
    }
    
    @Override
    public List<Record> findRecordsByDocumentId(Long documentId,int size,int page) {
        return checkPage(documentRepository.findRecordsByDocumentId(documentId,PageRequest.of(page, size)));
    }

    private Document check(Optional<Document> optional) {
        if (optional.isPresent()) {
            return optional.get();
        } else {
            return null;
        }
    }

    private <T> List<T> checkList(Optional<List<T>> optional) {
        if (optional.isPresent()) {
            return optional.get();
        } else {
            return null;
        }
    }

    private <T> List<T> checkPage(Optional<Page<T>> optional) {
        if (optional.isPresent()) {
            return optional.get().getContent();
        } else {
            return null;
        }
    }

    @Override
    public boolean containPart(Long partId, Long documentId) {
        Optional<Integer> optional=documentRepository.containPart(partId, documentId);
        if (optional.isPresent() && optional.get()!=0)
            return true;
        return false;
    }

    @Override
    public boolean hasAsses(Long documentId, Long personId) {
        Optional<Integer> optional=documentRepository.hasAsses(documentId, personId);
        if (optional.isPresent() && optional.get()!=0)
            return true;
        return false;
    }

}
