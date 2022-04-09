package org.eimsystems.chat;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;


public class UserDetailsImpl implements UserDetails {

    private final User user;
    public UserDetailsImpl(User user){
        super();
        this.user = user;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return ""+user.getPw();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    public Long getId(){
        return user.getId();
    }
    public String getName(){
        return user.getName();
    }
    public String getFirstName(){
        return user.getFirstName();
    }
    public Date getBirthday(){
        return user.getBirthday();
    }
    public String getTel(){
        return user.getTel();
    }
    public String getEmail(){
        return user.getEmail();
    }
    public Locale getLocale(){
        return user.getLocal();
    }
    public byte[] getProfilePicture(){
        return  user.getProfilePicture();
    }
}
