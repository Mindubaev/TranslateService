/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Artur
 */
@Table(name = "Person")
@Entity(name = "Person")
public class Person implements Serializable,UserDetails{

    private Long id;
    @JsonIgnore
    private String login;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private List<Project> ownProjects;
    @JsonIgnore
    private List<Project> projects;
    @JsonIgnore
    private List<Message> messages;
    @JsonIgnore
    private List<Record> records;
    @JsonIgnore
    private List<Comment> comments;
    private String name;

    public Person(String login, String password, List<Project> ownProjects, List<Project> projects, List<Message> messages, List<Record> records, List<Comment> comments, String name) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.ownProjects = ownProjects;
        this.projects = projects;
        this.messages = messages;
        this.records = records;
        this.comments = comments;
        this.name = name;
    }

    public Person(Long id, String login, String password, List<Project> ownProjects, List<Project> projects, List<Message> messages, List<Record> records, List<Comment> comments, String name) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.ownProjects = ownProjects;
        this.projects = projects;
        this.messages = messages;
        this.records = records;
        this.comments = comments;
        this.name = name;
    }

    public Person() {
    }
    
    @Id
    @org.springframework.data.annotation.Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    @Column(name = "login")
    public String getLogin() {
        return login;
    }

    @Column(name = "password")
    @Override
    public String getPassword() {
        return password;
    }

    @OneToMany(mappedBy = "person",
            fetch = FetchType.LAZY, 
            orphanRemoval = true,
            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}
    )
    public List<Project> getOwnProjects() {
        return ownProjects;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    @OneToMany(mappedBy = "person",
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}
    )
    @JoinTable(name = "Person_project_relation", 
            joinColumns = @JoinColumn(name = "personId"),
            inverseJoinColumns = @JoinColumn(name="projectId")
    )
    public List<Project> getProjects() {
        return projects;
    }

    @OneToMany(mappedBy = "person",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},
            orphanRemoval = true
    )
    public List<Message> getMessages() {
        return messages;
    }

    @OneToMany(mappedBy = "person",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},
            orphanRemoval = true
    )
    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public void setOwnProjects(List<Project> ownProjects) {
        this.ownProjects = ownProjects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    @Transient
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("USER"));
    }

    @JsonIgnore
    @Transient
    @Override
    public String getUsername() {
        return getLogin();
    }

    @JsonIgnore
    @Transient
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Transient
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Transient
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Transient
    @Override
    public boolean isEnabled() {
        return true;
    }
    
}
