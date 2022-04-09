package org.eimsystems.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.Frequency;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MethodTests {

    @Autowired
    private ChatService controller;
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    private String host = "http://localhost:";

    private final ObjectMapper objectMapper = new ObjectMapper();

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }

    HttpHeaders createHeaders(String username, String password){
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")) );
            String authHeader = "Basic " + new String( encodedAuth );
            set( "Authorization", authHeader );
            setContentType(MediaType.APPLICATION_JSON);
        }};
    }

    private void register(User user) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //restTemplate.withBasicAuth("admin", "-52617293");
        System.out.println(user.toString());
        HttpEntity<String> request = new HttpEntity<String>(mapToJson(user), headers);
        Boolean result = restTemplate.postForObject(host+port+"/register", request, Boolean.class);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result);
    }

    private void deregister(User user) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //restTemplate.withBasicAuth("admin", "-52617293");
        System.out.println(user.toString());
        HttpEntity<String> request = new HttpEntity<String>(mapToJson(user), headers);
        Boolean result = restTemplate.postForObject(host+port+"/deregister", request, Boolean.class);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result);
    }
    private User getRandUser(){
        return TestUtils.getRandUser();
    }


    @Test
    @Order(1)
    void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    /**
     * registers a user and then, deregister
     */
    @Test
    @Order(2)
    void registerTest() throws JsonProcessingException {
        User user = getRandUser();
        register(user);
        deregister(user);
    }

    @Test
    @Order(3)
    void adminLoginTest() throws JsonProcessingException{
        ResponseEntity<User> user = restTemplate.exchange(host+port+"/user", HttpMethod.GET,new HttpEntity<>(createHeaders("admin", "123456789")),User.class );
        System.out.println("status-code: " + user.getStatusCode());
        assertNotNull(user);
        System.out.println(user);
        assertTrue(user.hasBody());
        assertNotNull(user.getBody());
    }

    /*
    registers user, login and deregister
     */
    @Test
    @Order(4)
    void loginTest() throws JsonProcessingException{
        User user0 = getRandUser();
        register(user0);

        ResponseEntity<User> user = restTemplate.exchange(host+port+"/user", HttpMethod.GET,new HttpEntity<>(createHeaders(user0.getUsername(), String.valueOf(user0.getPw()))),User.class );
        System.out.println("status-code: " + user.getStatusCode());
        assertNotNull(user);
        System.out.println(user);
        assertTrue(user.hasBody());
        assertNotNull(user.getBody());

        deregister(user0);
    }
    /**
     * registers 2 users and send message from first to second
     */
    @Disabled
    @Test
    void messageTest() throws JsonProcessingException {
        //todo
        User user0 = getRandUser();
        User user1 = getRandUser();
        if(user0.equals(user1))return;
        register(user0);
        register(user1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //todo basicAuth
        HttpEntity<String> loginRequest0 = new HttpEntity<String>(headers);
        User user0l = restTemplate.postForObject(host+port+"/login", loginRequest0, User.class);
        assertNotNull(user0l);



        //HttpEntity<String> request = new HttpEntity<String>(mapToJson(user), headers);
        //restTemplate.postForObject(uri+"/sendMessage", req)

        deregister(user0);
        deregister(user1);
    }

    /*
    registers 2 users and one searchs for the first
     */
    @Test
    void searchTest() throws JsonProcessingException{
        //todo
    }

    @Test
    void testTest(){
        ResponseEntity<Integer> i = restTemplate.exchange(host+port+"/test", HttpMethod.GET,new HttpEntity<>(createHeaders("admin", "123456789")),Integer.class );
        System.out.println("status-code: " + i.getStatusCode());
        assertNotNull(i);
        System.out.println(i);
        assertTrue(i.hasBody());
        assertNotNull(i.getBody());
    }
}
