package org.eimsystems.chat;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class ChatService {
	Logger logger;
	/*
	 * central class for handling methods
	 */
	private final PersistenceService persistenceService =  new PersistenceService();

	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}

	protected <T> T mapFromJson(String json, Class<T> clazz) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(User.class, new UserEditor());
	}


	@RequestMapping(value = "/user", method = RequestMethod.GET)
	@ResponseBody
	public User currentUser() {
		logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		logger.setLevel(Level.ALL);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication.getPrincipal() instanceof UserDetails userDetails) {
			logger.info("authorities: " + userDetails.getAuthorities());
			logger.info("userdetails username: " + userDetails.getUsername());
			return persistenceService.getUser(userDetails.getUsername());
		}
		logger.severe("fatal problem in /user");
		return null;
	}


	@PostMapping(value = "/sendmessage", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void sendMessage(Message message) throws JsonProcessingException {
		//ObjectMapper objectMapper = new ObjectMapper();
		//Message message = objectMapper.readValue(strMessage, Message.class);
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
	@PostMapping( value = "/getMessage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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

	@GetMapping(value ="/test", produces = MediaType.APPLICATION_JSON_VALUE)
	public int test(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		logger.info("authentication: " + authentication.toString());
		String currentUser = authentication.getName();
		logger.info("currentUser: " + currentUser);

		if(authentication.getPrincipal() instanceof UserDetails userDetails){
			logger.info("authorities: " + userDetails.getAuthorities());
			logger.info("userdetails username: " + userDetails.getUsername());
			logger.info("userdetails password: " + userDetails.getPassword());
		}else if(authentication.getPrincipal() instanceof String s){
			logger.info("principal is instanceof String, principal : " + s);
		}

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.
				currentRequestAttributes()).
				getRequest();
		logger.info("AuthType: " + request.getAuthType());
		logger.info("remoteUser: " + request.getRemoteUser());
		logger.info(("username header: " + request.getHeader("username")));
		logger.info("password header: " + request.getHeader("password"));

		logger.info(authentication.getPrincipal().getClass().getName());
		return 0;
	}
}
