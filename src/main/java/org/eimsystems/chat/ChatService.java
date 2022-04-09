package org.eimsystems.chat;


import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class ChatService {
	/*
	 * central class for handling methods
	 */
	private final PersistenceService persistenceService =  new PersistenceService();


	public List<Message> getMessage(){
		
		
		/* 
		 * do the stuff
		 */
		return null;
	}
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(User.class, new UserEditor());
	}

	//todo probably it does not consumes any
	@PostMapping(value ="/login",  consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public User login(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String name = authentication.getName();
		return persistenceService.getUser(name);
	}

	@GetMapping("/sendMessage")
	public void sendMessage(@RequestParam(value="message", defaultValue = "null") Message message) {
		QueueService.store(message, message.getIdReceive());
	}

	@PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public boolean register(@RequestBody User user) {
		return persistenceService.register(user);
	}
	@PostMapping(value ="/deregister",  consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public boolean deregister(@RequestBody User user){
		return persistenceService.deregister(user);
	}
	@PostMapping(value ="/updateuser",  consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public User updateUSer(User user){
		return persistenceService.updateUser(user);
	}
	

	@GetMapping("/search")
	public User search(@RequestParam(value="search", defaultValue = "") String s) {
		//todo use username, mail or tel to find user and return
		return null;
	}
	

	@GetMapping("/ping")
	public long ping() {
		Logger logger =  Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		logger.setLevel(Level.ALL);
		logger.info("####");
		logger.info("/ping: " + System.currentTimeMillis());
		return System.currentTimeMillis();
	}

	@PostMapping(value = "/storeMessage", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void storeMessages(ArrayList<Message> messages){
		for(Message message: messages){
			QueueService.store(message, message.getIdReceive());
		}
	}
	@GetMapping(value = "/getMessage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Message[] getMessages(long id){
		return (Message[])QueueService.get(id).toArray();
	}


	@PostMapping(value = "/storeKey", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void storeKey(KeyExchange keyExchange){
		persistenceService.persistKey(keyExchange);
	}

	/**
	 * call method in some intervals to retrieve  new public Keys
	 * @param id used to get Keys
	 * @return List of KeyExchange
	 */
	@GetMapping(value = "/getKeys", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<KeyExchange> getKeys(long id){
		return persistenceService.getKeyExchange(id);
	}
}
