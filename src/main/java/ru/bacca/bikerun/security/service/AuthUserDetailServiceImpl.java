package ru.bacca.bikerun.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.bacca.bikerun.entity.AuthUser;
import ru.bacca.bikerun.repository.AuthUserRepository;
import ru.bacca.lvlup.entity.User;
import ru.bacca.lvlup.repository.UserRepository;

import javax.transaction.Transactional;

@Service
public class AuthUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    AuthUserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        AuthUser user = userRepository.findAuthUserByLogin(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User Not Found with -> username or email : " + username)
                );

        return AuthUserPrinciple.build(user);


    }
}