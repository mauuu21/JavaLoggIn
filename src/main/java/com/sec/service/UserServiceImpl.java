package com.sec.service;


import com.sec.repository.RoleRepository;
import com.sec.entity.User;
import com.sec.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final String USER_ROLE = "USER";

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(UserDetailsImpl::new)
                .orElseThrow(()->new RuntimeException(username));
    }

    public String registerUser(User userToRegister) {
        if (userRepository.findByEmail(userToRegister.getEmail()).isPresent()) {
            throw new RuntimeException("User already defined!: " + userToRegister.getEmail());
        } else {
            Optional.ofNullable(roleRepository.findByRole(USER_ROLE))
                    .ifPresentOrElse(
                            userRole -> userToRegister.getRoles().add(userRole),
                            () -> userToRegister.addRoles(USER_ROLE)
                    );
            userToRegister.setEnabled(false);
            userToRegister.setActivation(generateKey());
            User u = userRepository.save(userToRegister);
            }
//            Role userRole = roleRepository.findByRole(USER_ROLE);
//
//            if (userRole != null) {
//                user.getRoles().add(userRole);
//            } else {
//                user.addRoles(USER_ROLE);
//            }
    return "ok";
    }

    public String generateKey() {
        String key = "";
        Random random = new Random();
        char[] activationPass = new char[16];
        for (int i = 0; i < activationPass.length; i++) {
            activationPass[i] = (char) ('a' + random.nextInt(26));
        }
        return new String(activationPass);
    }

    public String userActivation(String code) {
        return Optional.ofNullable(userRepository.findByActivation(code))
                .map(user -> {
                    user.setEnabled(true);
                    user.setActivation("");
                    userRepository.save(user);
                    return "ok";
                })
                .orElse("noresult");

//        User user = userRepository.findByActivation(code);
//        if (user == null)
//            return "noresult";
//
//        user.setEnabled(true);
//        user.setActivation("");
//        userRepository.save(user);
//        return "ok";
    }

};






//    @Override                                 //ua.
//    public UserDetails loadUserByUsername(String username) {
//        User user = findByEmail(username);
//        if (user == null) {
//            throw new UsernameNotFoundException(username);
//        }
//        return new UserDetailsImpl(user);
//    }
//
//    public User findByEmail(String email) {
//        return userRepository.findByEmail(email);
//    }
