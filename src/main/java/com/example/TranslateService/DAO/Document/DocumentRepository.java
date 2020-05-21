/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.DAO.Document;

import com.example.TranslateService.Entities.Document;
import com.example.TranslateService.Entities.Record;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Artur
 */
public interface DocumentRepository extends JpaRepository<Document, Long>{
    
    Optional<List<Document>> findByProjectIdOrderByIdAsc(Long id); 
    Optional<Page<Document>> findByProjectIdOrderByIdAsc(Long id,Pageable pageable);
    @Query(nativeQuery = true,
            value = "select count(*) from Part where id=?1 documentId=?1" )
    Optional<Integer> containPart(Long partId,Long documentId);
    @Query(nativeQuery = true,
            value = "select count(person.id)+" +
            "(select count(person.id) from document " +
            "left join project on document.project_id=project.id  " +
            "left join person on project.person_id=person.id  " +
            "where document.id=1 and person.id=1) from document " +
            "left join project on document.project_id=project.id " +
            "left join person_project_relation on project.id=person_project_relation.project_id " +
            "left join person on person_project_relation.person_id=person.id " +
            "where document.id=?1 and person.id=?2")
    Optional<Integer> hasAsses(Long documentId,Long personId);
    
    @Query(nativeQuery = false,
            value="select r from Document d "
                    + "left join d.history h "
                    + "left join h.records r "
                    + "where d.id=?1 "
                    + "order by r.id desc"
    )
    List<Record> findRecordsByDocumentId(Long documentId); 
    @Query(nativeQuery = false,
            value="select r from Document d "
                    + "left join d.history h "
                    + "left join h.records r "
                    + "where d.id=?1 "
                    + "order by r.id desc "
    )
    Optional <Page<Record>> findRecordsByDocumentId(Long documentId,Pageable pageable); 
    
}
