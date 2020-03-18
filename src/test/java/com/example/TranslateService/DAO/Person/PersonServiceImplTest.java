/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.TranslateService.DAO.Person;

import com.example.TranslateService.Entities.Person;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author Artur
 */
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.NONE,replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(SpringExtension.class)
@DataJpaTest()
public class PersonServiceImplTest {
    
    public PersonServiceImplTest() {
    }
//    
//    @BeforeAll
//    public static void setUpClass() {
//    }
//    
//    @AfterAll
//    public static void tearDownClass() {
//    }
//    
//    @BeforeEach
//    public void setUp() {
//    }
//    
//    @AfterEach
//    public void tearDown() {
//    }
//
//    /**
//     * Test of save method, of class PersonServiceImpl.
//     */
//    @Test
//    public void testSave() {
//        System.out.println("save");
//        Person person = null;
//        PersonServiceImpl instance = new PersonServiceImpl();
//        Person expResult = null;
//        Person result = instance.save(person);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of delete method, of class PersonServiceImpl.
//     */
//    @Test
//    public void testDelete() {
//        System.out.println("delete");
//        Person person = null;
//        PersonServiceImpl instance = new PersonServiceImpl();
//        instance.delete(person);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testFindById() {
//        System.out.println("findById");
//        Long id = new Long(1);
//        PersonServiceImpl instance = new PersonServiceImpl();
//        Person result = instance.findById(id);
//        assertNotEquals(null, result);
//    }
//
//    /**
//     * Test of findByLoginAndPassword method, of class PersonServiceImpl.
//     */
//    @Test
//    public void testFindByLoginAndPassword() {
//        System.out.println("findByLoginAndPassword");
//        String login = "";
//        String password = "";
//        PersonServiceImpl instance = new PersonServiceImpl();
//        Person expResult = null;
//        Person result = instance.findByLoginAndPassword(login, password);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of findAll method, of class PersonServiceImpl.
//     */
//    @Test
//    public void testFindAll_0args() {
//        System.out.println("findAll");
//        PersonServiceImpl instance = new PersonServiceImpl();
//        List<Person> expResult = null;
//        List<Person> result = instance.findAll();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of findAll method, of class PersonServiceImpl.
//     */
//    @Test
//    public void testFindAll_Pageable() {
//        System.out.println("findAll");
//        Pageable pageable = null;
//        PersonServiceImpl instance = new PersonServiceImpl();
//        List<Person> expResult = null;
//        List<Person> result = instance.findAll(pageable);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
