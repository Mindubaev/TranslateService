/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.Entities;

import java.util.Date;

/**
 *
 * @author Artur
 */
public class CommentData {
    
    private Long id;
    private Person person;
    private Long partId;
    private String text;
    private Date date;

    public CommentData() {
    }
    
    public CommentData(Comment comment){
        this.id=comment.getId();
        this.person=comment.getPerson();
        this.partId=comment.getPart().getId();
        this.text=comment.getText();
        this.date=comment.getDate();
    }

    public Long getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public Long getPartId() {
        return partId;
    }

    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
}
