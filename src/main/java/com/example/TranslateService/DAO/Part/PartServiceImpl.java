/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.DAO.Part;

import com.example.TranslateService.Entities.Part;
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
@Service("partService")
@Repository
public class PartServiceImpl implements PartService{
    
    @Autowired
    private PartRepository partRepository;

    public PartServiceImpl(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    @Override
    public Part save(Part part) {
        return partRepository.save(part);
    }

    @Override
    public void delete(Part part) {
        partRepository.delete(part);
    }

    @Transactional(readOnly = true)
    @Override
    public Part findById(Long id) {
        Optional<Part> optional =partRepository.findById(id);
        if (optional.isPresent())
            return optional.get();
        else
            return null;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Part> findAll() {
        return partRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Part> findAll(Pageable pageable) {
        return partRepository.findAll(pageable).getContent();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Part> findByDocumentIdOrderByIdAsc(Long documentId) {
        return checkList(partRepository.findByDocumentIdOrderByIdAsc(documentId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Part> findByDocumentIdOrderByIdAsc(Long documentId, Pageable pageable) {
        return checkPage(partRepository.findByDocumentIdOrderByIdAsc(documentId, pageable));
    }
    
    private List<Part> checkList(Optional<List<Part>> optional) {
        if (optional.isPresent()) {
            return optional.get();
        } else {
            return null;
        }
    }

    private List<Part> checkPage(Optional<Page<Part>> optional) {
        if (optional.isPresent()) {
            return optional.get().getContent();
        } else {
            return null;
        }
    }
    
}
