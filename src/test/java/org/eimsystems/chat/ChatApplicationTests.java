package org.eimsystems.chat;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Repeat;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ChatApplicationTests {

    @Autowired
    private ChatService controller;
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }

    static List<User> getTestUser() {
        List<User> implementations = new ArrayList<User>();
        implementations.add(new User("Muster", "Max", "max.muster@email.com", "+4910000000000", new Locale("en"), new Date(System.currentTimeMillis()), "muster23085", "schlechtespw".hashCode()));
        implementations.add(new User("MusterX", "MaxX", "max.musterX@email.com", "+4910000000001", new Locale("de"), new Date(System.currentTimeMillis() - 10000), "muster23085X", "nocheinschlechtespw".hashCode()));
        return implementations;
    }



    static List<User> getRandTestUser() {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < new Random().nextInt(10); i++) {
            list.add(TestUtils.getRandUser());
        }
        list.add(TestUtils.getRandUser());
        return list;
    }

    /**@BeforeAll
    static void cleanUp(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myEntityManager");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("delete from chat.user");
        em.getTransaction().commit();
        em.close();
    }*/

    @Test
    @Order(1)
    void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }


    @Test
    @Order(2)
    void pingTest() throws Exception {
        long result = this.restTemplate.getForObject("http://localhost:" + port + "/ping", Long.class);
        assertThat(result).isLessThanOrEqualTo(System.currentTimeMillis());
        System.out.println(result);
    }

    @ParameterizedTest
    @Order(3)
    @MethodSource("getTestUser")
    void testRegister(User user) throws Exception {
        System.out.println("testRegister");
        String uri = "http://localhost:" + port + "/register";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //restTemplate.withBasicAuth("admin", "-52617293");
        System.out.println(user.toString());
        HttpEntity<String> request = new HttpEntity<String>(mapToJson(user), headers);
        Boolean result = restTemplate.postForObject(uri, request, Boolean.class);
        assertNotNull(result);
    }

    @ParameterizedTest
    @Order(4)
    @MethodSource("getTestUser")
    void testDeregister(User user) throws Exception {
        System.out.println("testDeregister ");
        String uri = "http://localhost:" + port + "/deregister";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //restTemplate.withBasicAuth("admin", "-52617293");
        System.out.println(user.toString());
        HttpEntity<String> request = new HttpEntity<String>(mapToJson(user), headers);
        Boolean result = restTemplate.postForObject(uri, request, Boolean.class);
        assertNotNull(result);
    }

    @ParameterizedTest
    @Order(5)
    @MethodSource("getRandTestUser")
    void testMultiDeRegister(User user) throws Exception {
        System.out.println("testMultiDeRegister");
        String uriD = "http://localhost:" + port + "/deregister";
        String uriR = "http://localhost:" + port + "/register";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //restTemplate.withBasicAuth("admin", "-52617293");
        System.out.println(user.toString());
        HttpEntity<String> request = new HttpEntity<String>(mapToJson(user), headers);
        Boolean result = restTemplate.postForObject(uriR, request, Boolean.class);
        assertNotNull(result);

        HttpEntity<String> request2 = new HttpEntity<String>(mapToJson(user), headers);
        Boolean result2 = restTemplate.postForObject(uriD, request2, Boolean.class);
        assertNotNull(result2);
    }

    @Disabled
    @ParameterizedTest
    @Order(6)
    @MethodSource("getRandTestUser")
    void randTestRegister(User user) throws Exception {
        System.out.println("randTestRegister");
        String uri = "http://localhost:" + port + "/register";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //restTemplate.withBasicAuth("admin", "-52617293");
        System.out.println(user.toString());
        HttpEntity<String> request = new HttpEntity<String>(mapToJson(user), headers);
        Boolean result = restTemplate.postForObject(uri, request, Boolean.class);
        assertNotNull(result);
        assertTrue(result);
    }

    @Disabled
    @ParameterizedTest
    @Order(7)
    @MethodSource("getRandTestUser")
    void randTestDeregister(User user) throws Exception {
        System.out.println("randTestDeregister");
        String uri = "http://localhost:" + port + "/deregister";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //restTemplate.withBasicAuth("admin", "-52617293");
        System.out.println(user.toString());
        HttpEntity<String> request = new HttpEntity<String>(mapToJson(user), headers);
        Boolean result = restTemplate.postForObject(uri, request, Boolean.class);
        assertNotNull(result);
        assertTrue(result);
    }

    @Disabled
    @ParameterizedTest
    @Order(8)
    @MethodSource("getRandTestUser")
    void testRandMultiDeRegister(User user) throws Exception {
        System.out.println("testRandMultiDeRegister");
        String uriD = "http://localhost:" + port + "/deregister";
        String uriR = "http://localhost:" + port + "/register";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //restTemplate.withBasicAuth("admin", "-52617293");
        System.out.println(user.toString());
        HttpEntity<String> request = new HttpEntity<String>(mapToJson(user), headers);
        Boolean result = restTemplate.postForObject(uriR, request, Boolean.class);
        assertNotNull(result);
        assertTrue(result);

        HttpEntity<String> request2 = new HttpEntity<String>(mapToJson(user), headers);
        Boolean result2 = restTemplate.postForObject(uriD, request2, Boolean.class);
        assertNotNull(result2);
        assertTrue(result2);
    }
}