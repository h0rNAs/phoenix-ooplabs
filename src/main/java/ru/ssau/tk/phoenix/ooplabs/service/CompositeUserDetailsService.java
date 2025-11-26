package ru.ssau.tk.phoenix.ooplabs.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CompositeUserDetailsService implements UserDetailsService {

    private final List<UserDetailsService> delegates;

    public CompositeUserDetailsService(UserDetailsService... delegates) {
        this.delegates = Arrays.asList(delegates);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        for (UserDetailsService delegate : delegates) {
            try {
                UserDetails user = delegate.loadUserByUsername(username);
                if (user != null) {
                    return user;
                }
            } catch (UsernameNotFoundException e) {

            }
        }
        throw new UsernameNotFoundException("Пользователь " + username + " не найден");
    }
}
