/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;

/**
 *
 * @author Artur
 */
@Entity(name = "Project")
@Table(name = "Project")
public class Project implements Serializable{
    
    private Long id;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Person person;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Person> persons;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Translation> translations;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Document> documents;
    private String name;

    public Project() {
    }

    public Project( Person person, List<Person> persons, List<Translation> translations, List<Document> documents, String name) {
        this.person = person;
        this.persons = persons;
        this.translations = translations;
        this.documents = documents;
        this.name = name;
    }

    public Project(Long id, Person person, List<Person> persons, List<Translation> translations, List<Document> documents, String name) {
        this.id = id;
        this.person = person;
        this.persons = persons;
        this.translations = translations;
        this.documents = documents;
        this.name = name;
    }

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    public Long getId() {
        return id;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="personId")
    public Person getPerson() {
        return person;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToMany(mappedBy = "projects", 
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}
    )
    public List<Person> getPersons() {
        return persons;
    }

    @OneToMany(mappedBy = "project",
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}
    )
    public List<Translation> getTranslations() {
        return translations;
    }

    @OneToMany(mappedBy = "project",
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}
    )
    public List<Document> getDocuments() {
        return documents;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public void setTranslations(List<Translation> translations) {
        this.translations = translations;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
    
}
