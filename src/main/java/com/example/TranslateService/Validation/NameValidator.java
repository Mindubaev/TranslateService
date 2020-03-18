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
public class NameValidator implements ConstraintValidator<NameConstraint, String>{
    
    private int min=4;
    private int max=20;
    
    @Override
    public void initialize(NameConstraint constraintAnnotation) {
        this.max=constraintAnnotation.max();
        this.min=constraintAnnotation.min();
    }
    
    public boolean isValid(String str){
        if (str==null)
            return false;
        else
        {
            if (str.length()<min)
            {
                return false;
            }
            else
            {
                if (str.length()>max)
                    return false;
                if (numOfSpecialCharactersInLogin(str)>0)
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
            if (!(Character.isAlphabetic(string.charAt(i))) && !(Character.isDigit(string.charAt(i))))
                counter++;
        }
        return counter;
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext ctx) {
        return isValid(string);
    }
    
}
