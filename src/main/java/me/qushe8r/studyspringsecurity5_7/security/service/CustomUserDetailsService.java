package me.qushe8r.studyspringsecurity5_7.security.service;

import lombok.RequiredArgsConstructor;
import me.qushe8r.studyspringsecurity5_7.domain.Account;
import me.qushe8r.studyspringsecurity5_7.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = userRepository.findByUsername(username);
        Account account = optionalAccount.orElseThrow(() -> {
            throw new UsernameNotFoundException("UsernameNotFoundException: " + username);
        });

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(account.getRole()));

        return new AccountContext(account, roles);
    }
}
