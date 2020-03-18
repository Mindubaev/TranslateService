/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.DAO.Message;

import com.example.TranslateService.Entities.Document;
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
@Transactional
@Repository
@Service("messageService")
public class MessageServiceImpl implements MessageService{
    
    @Autowired
    private MessageRepository messageRepository;

    public MessageServiceImpl() {
    }

    @Override
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public void delete(Message message) {
        messageRepository.delete(message);
    }

    @Transactional(readOnly = true)
    @Override
    public Message findById(Long id) {
        return check(messageRepository.findById(id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Message> findAll(Pageable pageable) {
        return messageRepository.findAll(pageable).getContent();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Message> findByDocumentIdOrderByDateAsc(Long id) {
        return checkList(messageRepository.findByDocumentIdOrderByDateAsc(id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Message> findByDocumentIdOrderByDateAsc(Long id, Pageable pageable) {
        return checkPage(messageRepository.findByDocumentIdOrderByDateAsc(id, pageable));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Message> findByPersonIdOrderByDateAsc(Long id) {
        return checkList(messageRepository.findByPersonIdOrderByDateAsc(id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Message> findByPersonIdOrderByDateAsc(Long id, Pageable pageable) {
        return checkPage(messageRepository.findByPersonIdOrderByDateAsc(id, pageable));
    }
    
    private Message check(Optional<Message> optional) {
        if (optional.isPresent()) {
            return optional.get();
        } else {
            return null;
        }
    }

    private List<Message> checkList(Optional<List<Message>> optional) {
        if (optional.isPresent()) {
            return optional.get();
        } else {
            return null;
        }
    }

    private List<Message> checkPage(Optional<Page<Message>> optional) {
        if (optional.isPresent()) {
            return optional.get().getContent();
        } else {
            return null;
        }
    }
    
}
