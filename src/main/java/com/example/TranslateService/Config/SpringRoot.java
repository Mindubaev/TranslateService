/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 *
 * @author Artur
 */
@Configuration
@ComponentScan(basePackages = "com.example.TranslateService")
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class SpringRoot {
    
    @Bean(name="multipartResolver")
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSizePerFile(500000000);
        return multipartResolver;
    }
    
}
