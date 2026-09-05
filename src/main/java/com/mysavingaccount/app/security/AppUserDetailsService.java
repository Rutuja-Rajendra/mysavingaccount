package com.mysavingaccount.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mysavingaccount.app.repository.UserRepository;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.mysavingaccount.app.entity.User appUser = userRepository.findByEmail(email);

        if (appUser == null) {
            System.out.println("No user found for that email!");

            throw new UsernameNotFoundException("No user found with email: " + email);
        }
        
        System.out.println("Found user, password hash in DB: " + appUser.getPassword());


        return User.builder()
                .username(appUser.getEmail())
                .password(appUser.getPassword())
                .authorities("USER")
                .build();
    }
}