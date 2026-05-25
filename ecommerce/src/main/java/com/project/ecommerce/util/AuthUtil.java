package com.project.ecommerce.util;

import com.project.ecommerce.entity.User;
import com.project.ecommerce.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    private UserRepository userRepository;

    public String loggedInEmail(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user=userRepository.findByUserName(authentication.getName())
                .orElseThrow(()->new UsernameNotFoundException("User not found with username: "+authentication.getName()));
        return user.getEmail();
    }

    public long loggedInUserId(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user=userRepository.findByUserName(authentication.getName())
                .orElseThrow(()->new UsernameNotFoundException("User not found with username: "+authentication.getName()));
        return user.getUserId();
    }

    public User loggedInUser(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user=userRepository.findByUserName(authentication.getName())
                .orElseThrow(()->new UsernameNotFoundException("User not found with username: "+authentication.getName()));
        return user;
    }
}
