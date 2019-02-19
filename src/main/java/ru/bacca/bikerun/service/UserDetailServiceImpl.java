package ru.bacca.bikerun.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.bacca.bikerun.entity.AuthUser;
import ru.bacca.bikerun.repository.AuthUserRepository;

@Service
public class UserDetailServiceImpl extends GenericServiceImpl<AuthUser, AuthUserRepository> implements UserDetailsService {


    public UserDetailServiceImpl(AuthUserRepository abstractJpaRepository) {
        super(abstractJpaRepository);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }
}
