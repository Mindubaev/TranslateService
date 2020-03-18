/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Artur
 */
@Table(name = "Part")
@Entity(name = "Part")
public class Part implements Serializable{
    
    @NotNull
    @Min(1)
    private Long id;
    @JsonIgnore
    private Document document;
    @NotBlank
    private String origin;
    @NotBlank
    private String translated;
    @JsonIgnore
    private List<Record> records; 

    public Part() {
    }

    public Part(Document document, String origin, String translated, List<Record> records) {
        this.document = document;
        this.origin = origin;
        this.translated = translated;
        this.records = records;
    }

    public Part(Long id, Document document, String origin, String translated, List<Record> records) {
        this.id = id;
        this.document = document;
        this.origin = origin;
        this.translated = translated;
        this.records = records;
    }

    @org.springframework.data.annotation.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documentId")
    public Document getDocument() {
        return document;
    }

    @Column(name="origin")
    public String getOrigin() {
        return origin;
    }

    @Column(name="translated")
    public String getTranslated() {
        return translated;
    }

    @OneToMany(mappedBy = "part",
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}
    )
    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setTranslated(String translated) {
        this.translated = translated;
    }
    
}
