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
@Component("passwordValidator")
public class PasswordValidator implements ConstraintValidator<PasswordConstraint, String>{
    
    public int max;
    public int min;
    public int minNumOfLetters;
    public int minNumOfDigits;
    public int maxNumOfSpecChar;

    @Override
    public void initialize(PasswordConstraint constraintAnnotation) {
        this.max=constraintAnnotation.max();
        this.min=constraintAnnotation.min();
        this.minNumOfDigits=constraintAnnotation.minNumOfDigits();
        this.minNumOfLetters=constraintAnnotation.minNumOfLetters();
        this.maxNumOfSpecChar=constraintAnnotation.maxNumOfSpecChar();
    }
    
    @Override
    public boolean isValid(String str, ConstraintValidatorContext ctx) {
        return isValid(str);
    }
    
    public boolean isValid(String str) {
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
                if (numOfSpecialCharactersInPassword(str)>maxNumOfSpecChar)
                    return false;
                if (str.contains(" "))
                    return false;
                if (numOfDigitsInPassword(str)<minNumOfDigits)
                    return false;
                if (numOfLetterInPassword(str)<minNumOfLetters)
                    return false;
            }
        }
        return true;
    }
    
    private int numOfDigitsInPassword(String string){
        char[] digits={'1','2','3','4','5','6','7','8','9','0'};
        int counter=0;
        for (int i=0;i<string.length();i++){
            boolean found=false;
            for(char digit:digits)
                if (string.charAt(i)==digit)
                {
                    found=true;
                    break;
                }
            if (found)
                counter=counter+1;
        }
        return counter;        
    }
    
    private int numOfLetterInPassword(String string){
        int counter=0;
        for (int i=0;i<string.length();i++){
            if (Character.isLetter(string.charAt(i)))
                counter++;
        }
        return counter;
    }
    
    private int numOfSpecialCharactersInPassword(String string){
        int counter=0;
        for (int i=0;i<string.length();i++){
            if (!(Character.isLetter(string.charAt(i))) && !(Character.isDigit(string.charAt(i))))
                counter++;
        }
        return counter;
    }
    
}
