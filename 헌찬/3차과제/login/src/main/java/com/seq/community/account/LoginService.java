package com.seq.community.account;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LoginService implements UserDetailsService{
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var result = accountRepository.findByUsername(username);

        if(result.isEmpty()) {
            throw new UsernameNotFoundException("user not found");
        }

        var user = result.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("일반"));

        var a = new CustomUser(user.getUsername(), user.getPassword(), authorities);
        return a;
    }
}

class CustomUser extends User{
    public CustomUser(
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(username, password, authorities);
    }
}