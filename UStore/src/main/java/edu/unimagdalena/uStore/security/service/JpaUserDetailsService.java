
package edu.unimagdalena.uStore.security.service;

import edu.unimagdalena.uStore.security.repo.AppUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class JpaUserDetailsService implements UserDetailsService{
    private final AppUserRepository appUserRepository;

    public JpaUserDetailsService(AppUserRepository appUserRepository){
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        var user = appUserRepository.findByEmailIgnoreCase(username).orElseThrow(() ->
                   new UsernameNotFoundException("User "+ username+ " not found"));

        var authorities = user.getRoles().stream().map(Enum::name).map(SimpleGrantedAuthority::new)
                              .collect(Collectors.toSet());

        return User.withUsername(user.getUsername())
                   .password(user.getPassword())
                   .authorities(authorities)
                   .build();
    }
}
