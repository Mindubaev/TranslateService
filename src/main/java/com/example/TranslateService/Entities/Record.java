/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
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
import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;

/**
 *
 * @author Artur
 */
@Table(name = "Record")
@Entity(name = "Record")
public class Record implements Serializable{
    
    private Long id;
    @JsonIgnore
    private History history;
    @JsonIgnore
    private Part part;
    private Person person;
    private Date date;
    private String text;

    public Record() {
    }

    public Record(History history, Part part, Person person, Date date, String text) {
        this.id = id;
        this.history = history;
        this.part = part;
        this.person = person;
        this.date = date;
        this.text = text;
    }

    public Record(Long id, History history, Part part, Person person, Date date, String text) {
        this.id = id;
        this.history = history;
        this.part = part;
        this.person = person;
        this.date = date;
        this.text = text;
    }
    public Record(Long id,String text,Date date) {
        this.id = id;
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
    @JoinColumn(name = "historyId")
    public History getHistory() {
        return history;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="partId")
    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setHistory(History history) {
        this.history = history;
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
    
    public RecordData toRecordData(){
        return new RecordData(id, part.getOrigin(), person, date, text);
    }
    
    public static class RecordData{
        
        private Long id;
        private String origin;
        private Person person;
        private Date date;
        private String text;

        public RecordData(Long id, String origin, Person person, Date date, String text) {
            this.id = id;
            this.origin = origin;
            this.person = person;
            this.date = date;
            this.text = text;
        }

        public RecordData() {
        }

        public Long getId() {
            return id;
        }

        public String getOrigin() {
            return origin;
        }

        public Person getPerson() {
            return person;
        }

        public Date getDate() {
            return date;
        }

        public String getText() {
            return text;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
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
    
}
