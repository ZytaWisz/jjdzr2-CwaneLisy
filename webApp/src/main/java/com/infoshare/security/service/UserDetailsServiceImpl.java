package com.infoshare.security.service;

import com.infoshare.security.UsersRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<com.infoshare.domain.User> optionalUser = usersRepository.findByUserEmail(userName);
        if(optionalUser.isPresent()) {
            com.infoshare.domain.User users = optionalUser.get();

            return User.builder()
                .username(users.getUserName())
                //change here to store encoded password in db
                //.password( bCryptPasswordEncoder.encode(users.getHashPassword()) )
                .password(users.getHashPassword())
                .disabled(false)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .roles("USER")
                .build();
        } else {
            throw new UsernameNotFoundException("User Name is not Found");
        }
    }
}
