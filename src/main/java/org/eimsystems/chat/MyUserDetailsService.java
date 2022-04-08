package org.eimsystems.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService  implements UserDetailsService {
    @Autowired
    private UserRepository repo;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        User user = repo.findByUsername(s);
        if(user==null){
            throw new UsernameNotFoundException("User 404");
        }
        return new UserDetailsImpl(user);
    }
}
