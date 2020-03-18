/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.Validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author Artur
 */
public class DocumentNameValidator implements ConstraintValidator<DocumentNameConstraint, String>{

    @Override
    public boolean isValid(String docName, ConstraintValidatorContext arg1) {
        return isValid(docName);
    }
    
    public boolean isValid(String docName) {
        return (docName!=null && (docName.endsWith(".txt") || docName.endsWith(".doc") || docName.endsWith(".docx")) && docName.lastIndexOf(".")!=0);
    }
    
}
