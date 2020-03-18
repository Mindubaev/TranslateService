/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.Validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 *
 * @author Artur
 */
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConstraint {
    int max() default 100;
    int min() default 0;
    int minNumOfLetters() default 0;
    int minNumOfDigits() default 0;
    int maxNumOfSpecChar() default 0;
    String message() default "unavailable password";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
