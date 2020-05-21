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
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Artur
 */
public interface CommentRepository extends JpaRepository<Comment,Long>{
    
    Optional<List<Comment>> findByPersonIdOrderByDateAsc(Long personId);
    Optional<Page<Comment>> findByPersonIdOrderByDateAsc(Long personId,Pageable pageable);
    Optional<List<Comment>> findByPartIdOrderByDateAsc(Long partId);
    Optional<Page<Comment>> findByPartIdOrderByDateAsc(Long partId,Pageable pageable);
    
}
