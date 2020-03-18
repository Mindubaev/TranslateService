/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.Controllers;

import com.example.TranslateService.DAO.Message.MessageService;
import com.example.TranslateService.DAO.Person.PersonService;
import com.example.TranslateService.DAO.Project.ProjectService;
import com.example.TranslateService.DAO.Records.RecordService;
import com.example.TranslateService.Entities.Message;
import com.example.TranslateService.Entities.Person;
import com.example.TranslateService.Entities.Project;
import com.example.TranslateService.Entities.Record;
import com.example.TranslateService.Validation.LoginConstraint;
import com.example.TranslateService.Validation.LoginValidator;
import com.example.TranslateService.Validation.NameConstraint;
import com.example.TranslateService.Validation.NameValidator;
import com.example.TranslateService.Validation.PasswordConstraint;
import com.example.TranslateService.Validation.PasswordValidator;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

/**
 *
 * @author Artur
 */
@Validated
@RestController
@CrossOrigin(origins = "*",allowCredentials = "true",allowedHeaders = "*",methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.PATCH,RequestMethod.DELETE})
public class PersonController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PersonService personService;
    @Autowired
    private MessageService MessageService;
    @Autowired
    private ProjectService ProjectService;
    @Autowired
    private RecordService recordService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean isAlreadyExist(String str) {
        return personService.findByLogin(str) != null;
    }

    @PostMapping("/person")
    public ResponseEntity<Person> registration(@RequestBody @Valid RegistrationForm form) {
        if (!isAlreadyExist(form.getUsername())) {
            Person person = personService.save(form.toPerson());
            return new ResponseEntity<Person>(person, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<Person>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Person> login(@Valid @RequestBody LoginForm form) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return new ResponseEntity<Person>(personService.findByLogin(form.getUsername()), HttpStatus.OK);
        } catch (AuthenticationException ex) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/person/{id}")
    public ResponseEntity<Person> getPerson(@PathVariable @Min(1) Long id) {
        return new ResponseEntity<Person>(personService.findById(id), HttpStatus.FOUND);
    }

    @GetMapping("/person/{id}/messages")
    public ResponseEntity<List<Message>> getPersonMessages(@PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person) {
        if (id.equals(person.getId())) {
            return new ResponseEntity<List<Message>>(MessageService.findByPersonIdOrderByDateAsc(id), HttpStatus.FOUND);
        } else {
            return new ResponseEntity<List<Message>>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/person/{id}/messages/pageable")
    public ResponseEntity<List<Message>> getPersonMessages(@PathVariable @Min(1) Long id,
            @RequestParam @Min(0) int page,
            @RequestParam @Min(0) int size,
            @AuthenticationPrincipal Person person) {
        if (id.equals(person.getId())) {
            return new ResponseEntity<List<Message>>(MessageService.findByPersonIdOrderByDateAsc(id, PageRequest.of(page, size)), HttpStatus.FOUND);
        } else {
            return new ResponseEntity<List<Message>>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/person/{id}/ownProjects")
    public ResponseEntity<List<Project>> getPersonOwnProjects(@PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person) {
        if (id.equals(person.getId())) {
            return new ResponseEntity<List<Project>>(ProjectService.findByPersonId(id), HttpStatus.FOUND);
        } else {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/person/{id}/ownProjects/pageable")
    public ResponseEntity<List<Project>> getPersonOwnProjects(@PathVariable @Min(1) Long id,
            @RequestParam @Min(0) int page,
            @RequestParam @Min(0) int size,
            @AuthenticationPrincipal Person person) {
        if (id.equals(person.getId())) {
            return new ResponseEntity<List<Project>>(ProjectService.findByPersonId(id, PageRequest.of(page, size)), HttpStatus.FOUND);
        } else {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @Transactional
    @GetMapping("/person/{id}/projects")
    public ResponseEntity<List<Project>> getPersonProjects(@PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person) {
        person = personService.findById(person.getId());////TransactionManager should get open session 
        if (id.equals(person.getId())) {
            return new ResponseEntity<List<Project>>(person.getProjects(), HttpStatus.FOUND);
        } else {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @Transactional
    @GetMapping("/person/{id}/projects/pageable")
    public ResponseEntity<List<Project>> getPersonProjects(@PathVariable @Min(1) Long id,
            @RequestParam @Min(0) int page,
            @RequestParam @Min(0) int size,
            @AuthenticationPrincipal Person person) {
        person = personService.findById(person.getId());//TransactionManager should get open session 
        if (id.equals(person.getId())) {
            int listSize = person.getProjects().size();
            int start = page * size;
            if (start >= listSize) {
                return new ResponseEntity(new ArrayList<>(), HttpStatus.FOUND);
            }
            int finish = start + size;
            if (finish >= listSize) {
                finish = listSize;
            }
            return new ResponseEntity(person.getProjects().subList(start, finish), HttpStatus.FOUND);
        } else {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/person/{id}/records")
    public ResponseEntity<List<Record>> getPersonRecords(@PathVariable @Min(1) Long id,
            @AuthenticationPrincipal Person person) {
        if (id.equals(person.getId())) {
            return new ResponseEntity<List<Record>>(recordService.findByPersonIdOrderByDateAsc(id), HttpStatus.FOUND);
        } else {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/person/{id}/records/pageable")
    public ResponseEntity<List<Record>> getPersonRecords(@PathVariable @Min(1) Long id,
            @RequestParam @Min(0) int page,
            @RequestParam @Min(0) int size,
            @AuthenticationPrincipal Person person) {
        if (id.equals(person.getId())) {
            return new ResponseEntity<List<Record>>(recordService.findByPersonIdOrderByDateAsc(id, PageRequest.of(page, size)), HttpStatus.FOUND);
        } else {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @Transactional
    @DeleteMapping("/person")
    public ResponseEntity<Person> deletePerson(@AuthenticationPrincipal Person person) {
        person=personService.findById(person.getId());
        for (Project project:person.getOwnProjects())
            ProjectService.deleteProjectFromAllPerson(project.getId());
        personService.deleteProfileFromAllProjects(person.getId());
        personService.deleteById(person.getId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @Transactional
    @PatchMapping("/person")
    public ResponseEntity<Person> patchPerson(@RequestBody RegistrationForm form,
            @AuthenticationPrincipal Person person) throws NoSuchFieldException {
        person = personService.findById(person.getId());
        if (form.getUsername()!=null){
            if (!form.getUsername().equals(person.getLogin()) && isAlreadyExist(form.getUsername()))
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            LoginValidator validator=new LoginValidator();
            validator.initialize(LoginForm.class.getDeclaredField("username").getAnnotation(LoginConstraint.class));
            if (!validator.isValid(form.getUsername()))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            person.setLogin(form.getUsername());
        }
        if (form.getName() != null) {
            NameValidator nameValidator = new NameValidator();
            nameValidator.initialize(RegistrationForm.class.getDeclaredField("name").getAnnotation(NameConstraint.class));
            if (!nameValidator.isValid(form.getName())) 
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            person.setName(form.getName());
        }
        if (form.getPassword()!=null)
        {
            PasswordValidator validator=new PasswordValidator();
            validator.initialize(LoginForm.class.getDeclaredField("password").getAnnotation(PasswordConstraint.class));
            if (!validator.isValid(form.getPassword()))
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            person.setPassword(passwordEncoder.encode(form.getPassword()));
        }
        person=personService.save(person);
        return new ResponseEntity<Person>(person, HttpStatus.OK);
    }

    public static class LoginForm {

        @LoginConstraint(max = 30, min = 0, maxNumOfSpecChar = 0)
        private String username;
        @PasswordConstraint(max = 50, min = 0, minNumOfDigits = 0, minNumOfLetters = 0, maxNumOfSpecChar = 0)
        private String password;

        public LoginForm(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }

    public static class RegistrationForm extends LoginForm {

        @NameConstraint(min = 2, max = 50)
        private String name;

        public RegistrationForm(String username, String password, String name) {
            super(username, password);
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Person toPerson() {
            return new Person(getUsername(), getPassword(), new ArrayList<Project>(), new ArrayList<Project>(), new ArrayList<Message>(), new ArrayList<Record>(), getName());
        }

    }

}
