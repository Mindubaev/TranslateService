/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;

/**
 *
 * @author Artur
 */
@Entity(name = "Message")
@Table(name = "Message")
public class Message implements Serializable{
    
    private Long id;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Document document;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Person person;
    @NotNull
    @PastOrPresent
    private Date date;
    @NotBlank
    @Size(min = 1,max = 250)
    private String text;

    public Message() {
    }

    public Message(Long id, Document document, Person person, Date date, String text) {
        this.id = id;
        this.document = document;
        this.person = person;
        this.date = date;
        this.text = text;
    }

    @Id
    @org.springframework.data.annotation.Id
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="personId")
    public Person getPerson() {
        return person;
    }

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    public Date getDate() {
        return date;
    }

    @Column(name = "text")
    public String getText() {
        return text;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setText(String text) {
        this.text = text;
    }
    
}
