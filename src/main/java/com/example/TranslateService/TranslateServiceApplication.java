package com.example.TranslateService;

import com.example.TranslateService.DAO.Person.PersonService;
import com.example.TranslateService.DAO.Person.PersonServiceImpl;
import com.example.TranslateService.DAO.Project.ProjectService;
import com.example.TranslateService.Entities.Person;
import com.example.TranslateService.Entities.Project;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class TranslateServiceApplication implements CommandLineRunner{
    
    @Autowired
    public PersonServiceImpl personServiceImpl;
    @Autowired
    public PasswordEncoder passwordEncoder;
    @Autowired
    public PersonService personService;
    @Autowired
    public ProjectService projectService;

	public static void main(String[] args) {
		SpringApplication.run(TranslateServiceApplication.class, args);
	}

    @Transactional
    @Override
    public void run(String... args) throws Exception {
//        Person person=personService.findById(new Long(1));
//        String str=passwordEncoder.encode("admin");
//        if (passwordEncoder.matches("admin", person.getPassword()))
//            System.out.println("!!!!");
//        System.out.println("");
    }

}
