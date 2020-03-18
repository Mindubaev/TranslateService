/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.Entities;

import com.example.TranslateService.Validation.DocumentNameConstraint;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Artur
 */
@Entity(name = "Document")
@Table(name = "Document")
public class Document implements Serializable{
    
    private Long id;
    @JsonIgnore
    private Project project;
    @JsonIgnore
    private List<Message> messages;
    @JsonIgnore
    private List<Part> parts;
    @JsonIgnore
    private History history;
    @DocumentNameConstraint
    private String name;

    public Document() {
    }

    public Document( Project project, List<Message> messages, List<Part> parts, History history, String name) {
        this.project = project;
        this.messages = messages;
        this.parts = parts;
        this.history = history;
        this.name = name;
    }

    public Document(Long id, Project project, List<Message> messages, List<Part> parts, History history, String name) {
        this.id = id;
        this.project = project;
        this.messages = messages;
        this.parts = parts;
        this.history = history;
        this.name = name;
    }

    @org.springframework.data.annotation.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    public Long getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    public Project getProject() {
        return project;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToMany(mappedBy = "document",
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}
    )
    public List<Message> getMessages() {
        return messages;
    }

    @OneToMany(mappedBy = "document",
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}
    )
    public List<Part> getParts() {
        return parts;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @OneToOne(mappedBy = "document",
            orphanRemoval = true,
            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},
            fetch = FetchType.LAZY
    )
    public History getHistory() {
        return history;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHistory(History history) {
        this.history = history;
    }
    
}
