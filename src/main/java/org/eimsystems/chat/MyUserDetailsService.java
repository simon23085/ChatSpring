package org.eimsystems.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class MyUserDetailsService  implements UserDetailsService {
    @Autowired
    private UserRepository repo;

    @Autowired
    private WebApplicationContext applicationContext;

    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }



    @PostConstruct
    public void completeSetup() {
        repo = applicationContext.getBean(UserRepository.class);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.setLevel(Level.ALL);

        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("authentication: " + authentication.toString());
        String currentUser = authentication.getName();
        logger.info("currentUser: " + currentUser);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        logger.info("authorities: " + userDetails.getAuthorities());
        logger.info("userdetails username: " + userDetails.getUsername());
        logger.info("userdetails password: " + userDetails.getPassword());

        HttpServletRequest request =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        logger.info("AuthType: " + request.getAuthType());
        logger.info("remoteUser: " + request.getRemoteUser());
        logger.info(("username header: " + request.getHeader("username")));
        logger.info("password header: " + request.getHeader("password"));*/

        User user = repo.findByUsername(s);
        if(user==null){
            throw new UsernameNotFoundException("User 404");
        }
        return new UserDetailsImpl(user);
    }
}
