/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.DAO.Comment;

import com.example.TranslateService.Entities.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Artur
 */
public interface CommentService {
    
    Comment save(Comment comment);
    void delete(Comment comment);
    Comment findById(Long id);
    List<Comment> findAll();
    List<Comment> findAll(int page,int size);
    List<Comment> findByPersonIdOrderByDateAsc(Long personId);
    List<Comment> findByPersonIdOrderByDateAsc(Long personId,int page,int size);
    List<Comment> findByPartIdOrderByDateAsc(Long partId);
    List<Comment> findByPartIdOrderByDateAsc(Long partId,int page,int size);
    
}
