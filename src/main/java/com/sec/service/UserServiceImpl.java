package com.sec.service;

import com.sec.entity.User;
import com.sec.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return new UserDetailsImpl(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
