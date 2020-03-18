/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.DAO.Translation;

import com.example.TranslateService.Entities.Translation;
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
@Service("translationService")
@Repository
public class TranslationServiceImpl implements TranslationService{
    
    @Autowired
    private TranslationRepository translationRepository;

    public TranslationServiceImpl(TranslationRepository translationRepository) {
        this.translationRepository = translationRepository;
    }

    @Override
    public Translation save(Translation translation) {
        return translationRepository.save(translation);
    }

    @Override
    public void delete(Translation translation) {
        translationRepository.delete(translation);
    }

    @Transactional(readOnly = true)
    @Override
    public Translation findById(Long id) {
        Optional<Translation> optional=translationRepository.findById(id);
        if (optional.isPresent())
            return optional.get();
        else
            return null;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Translation> findAll() {
        return translationRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Translation> findAll(Pageable pageable) {
        return translationRepository.findAll(pageable).getContent();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Translation> findByProjectIdOrderByIdAsc(Long projectId) {
        return checkList(translationRepository.findByProjectIdOrderByIdAsc(projectId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Translation> findByProjectIdOrderByIdAsc(Long projectId, Pageable pageable) {
        return checkPage(translationRepository.findByProjectIdOrderByIdAsc(projectId, pageable));
    }
    
    private List<Translation> checkList(Optional<List<Translation>> optional) {
        if (optional.isPresent()) {
            return optional.get();
        } else {
            return null;
        }
    }

    private List<Translation> checkPage(Optional<Page<Translation>> optional) {
        if (optional.isPresent()) {
            return optional.get().getContent();
        } else {
            return null;
        }
    }
    
}
