/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Artur
 */
@Table(name = "Translation")
@Entity(name = "Translation")
public class Translation implements Serializable{
    
    private Long id;
    @NotNull
    @Min(1)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Project project;
    @NotBlank
    private String origin;
    @NotBlank
    private String translated;

    public Translation() {
    }

    public Translation(Long id, Project project, String origin, String translated) {
        this.id = id;
        this.project = project;
        this.origin = origin;
        this.translated = translated;
    }

    @org.springframework.data.annotation.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    public Project getProject() {
        return project;
    }

    @Column(name="origin")
    public String getOrigin() {
        return origin;
    }

    @Column(name="translated")
    public String getTranslated() {
        return translated;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setTranslated(String translated) {
        this.translated = translated;
    }
    
}
