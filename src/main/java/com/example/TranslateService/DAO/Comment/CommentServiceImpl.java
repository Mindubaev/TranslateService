/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.DAO.Comment;

import com.example.TranslateService.Entities.Comment;
import com.example.TranslateService.Entities.Message;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Artur
 */
@Transactional
@Repository
@Service("commentService")
public class CommentServiceImpl implements CommentService{
    
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    @Override
    public Comment findById(Long id) {
        Optional<Comment> optional = commentRepository.findById(id);
        if (optional.isPresent())
            return optional.get();
        return null;
    }

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> findAll(int page, int size) {
        return commentRepository.findAll(PageRequest.of(page, size)).toList();
    }

    @Override
    public List<Comment> findByPersonIdOrderByDateAsc(Long personId) {
        return checkList(commentRepository.findByPersonIdOrderByDateAsc(personId));
    }

    @Override
    public List<Comment> findByPersonIdOrderByDateAsc(Long personId, int page, int size) {
        return checkPage(commentRepository.findByPersonIdOrderByDateAsc(personId,PageRequest.of(page, size)));
    }

    @Override
    public List<Comment> findByPartIdOrderByDateAsc(Long partId) {
        return checkList(commentRepository.findByPartIdOrderByDateAsc(partId));
    }

    @Override
    public List<Comment> findByPartIdOrderByDateAsc(Long partId, int page, int size) {
        return checkPage(commentRepository.findByPartIdOrderByDateAsc(partId,PageRequest.of(page, size)));
    }
    
    private List<Comment> checkPage(Optional<Page<Comment>> optional) {
        if (optional.isPresent()) {
            return optional.get().getContent();
        } else {
            return null;
        }
    }
    
    private List<Comment> checkList(Optional<List<Comment>> optional) {
        if (optional.isPresent()) {
            return optional.get();
        } else {
            return null;
        }
    }
    
}
