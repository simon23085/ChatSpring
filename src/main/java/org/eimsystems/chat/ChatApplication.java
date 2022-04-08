package org.eimsystems.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.codec.json.Jackson2JsonEncoder;

import java.util.Date;
import java.util.Locale;

@SpringBootApplication
public class ChatApplication {
	static PersistenceService persistenceService =  new PersistenceService();
	public static void main(String[] args) {
		SpringApplication.run(ChatApplication.class, args);
		ObjectMapper objectMapper =  new ObjectMapper();
		User user = new User("Muster", "Max", "max.muster@email.com","+4910000000000",new Locale("en"), new Date(System.currentTimeMillis()), "muster23085", "badpw".hashCode());
		String json = "";
		try {
			json = objectMapper.writeValueAsString(user);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println(json);
	}

}
