/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Artur
 */
@Table(name = "History")
@Entity(name = "History")
public class History implements Serializable{
    
    private Long id;
    private Document document;
    private List<Record> records;

    public History() {
    }

    public History(Document document, List<Record> records) {
        this.document = document;
        this.records = records;
    }

    public History(Long id, Document document, List<Record> records) {
        this.id = id;
        this.document = document;
        this.records = records;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documentId")
    public Document getDocument() {
        return document;
    }

    @OneToMany(mappedBy = "history",
            orphanRemoval = true,
            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},
            fetch = FetchType.LAZY
    )
    public List<Record> getRecords() {
        return records;
    }

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }
    
}
