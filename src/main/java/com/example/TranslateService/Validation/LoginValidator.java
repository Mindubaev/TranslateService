/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.Validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author Artur
 */
public class LoginValidator implements ConstraintValidator<LoginConstraint, String>{
    
    private int min=4;
    private int max=20;
    private int maxNumOfSprcChar=0;
    
    @Override
    public void initialize(LoginConstraint constraintAnnotation) {
        this.max=constraintAnnotation.max();
        this.min=constraintAnnotation.min();
        this.maxNumOfSprcChar=constraintAnnotation.maxNumOfSpecChar();
    }
    
    @Override
    public boolean isValid(String str, ConstraintValidatorContext ctx) {
        return isValid(str);
    }
    
    public boolean isValid(String str){
        if (str==null)
            return false;
        else
        {
            if (str.length()<min)
                return false;
            else
            {
                if (str.length()>max)
                    return false;
                if (str.startsWith("@") || str.startsWith("."))
                return false;
                if (numOfSpecialCharactersInLogin(str)>maxNumOfSprcChar)
                    return false;
                if (str.contains(" "))
                    return false;
            }
        }
        return true;
    }
    
    private int numOfSpecialCharactersInLogin(String string){
        int counter=0;
        for (int i=0;i<string.length();i++){
            if (!(Character.isAlphabetic(string.charAt(i))) && !(Character.isDigit(string.charAt(i))) && string.charAt(i)!='@' && string.charAt(i)!='.')
                counter++;
        }
        return counter;
    }
    
}
