package ru.bacca.bikerun.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.bacca.bikerun.entity.AuthUser;
import ru.bacca.bikerun.repository.AuthUserRepository;

import java.util.Collections;

@Service
public class AuthUserService extends GenericServiceImpl<AuthUser, AuthUserRepository>{

    public AuthUserService(AuthUserRepository abstractJpaRepository) {
        super(abstractJpaRepository);
    }

    public UserDetails findAuthUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = repository.findByUsername(username);
        if (authUser == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(authUser.getUsername(), authUser.getPassword(), Collections.emptyList());
    }
}
