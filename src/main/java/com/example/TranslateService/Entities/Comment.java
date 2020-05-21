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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Artur
 */
@Table(name = "Comment")
@Entity(name = "Comment")
public class Comment implements Serializable{
    
    private Long id;
    private Person person;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Part part;
    @NotBlank
    @Size(min = 1,max = 250)
    private String text;
    @NotNull
    private Date date;

    public Comment(Person person, Part part, String text, Date date) {
        this.id = id;
        this.person = person;
        this.part = part;
        this.text = text;
        this.date = date;
    }

    public Comment(Long id, Person person, Part part, String text, Date date) {
        this.id = id;
        this.person = person;
        this.part = part;
        this.text = text;
        this.date = date;
    }

    public Comment() {
    }

    @Id
    @org.springframework.data.annotation.Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "personId")
    public Person getPerson() {
        return person;
    }

    @Column(name = "text")
    public String getText() {
        return text;
    }

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    public Date getDate() {
        return date;
    }

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "partId")
    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
}
