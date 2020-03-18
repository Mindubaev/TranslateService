/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.DAO.Document;

import com.example.TranslateService.Entities.Document;
import com.example.TranslateService.Entities.Project;
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

    private Document check(Optional<Document> optional) {
        if (optional.isPresent()) {
            return optional.get();
        } else {
            return null;
        }
    }

    private List<Document> checkList(Optional<List<Document>> optional) {
        if (optional.isPresent()) {
            return optional.get();
        } else {
            return null;
        }
    }

    private List<Document> checkPage(Optional<Page<Document>> optional) {
        if (optional.isPresent()) {
            return optional.get().getContent();
        } else {
            return null;
        }
    }

}
