package com.Service;


import com.Model.*;
import com.Repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class userDetailsService implements  UserDetailsService{

    @Autowired
    EmailRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user= repo.findByEmail(email);
        if(user.getEmail()==null)
        {
            throw new NullPointerException("No user found");
        }
        return new userDetailsImpl(user);
    }    
}
