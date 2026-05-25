package com.tpe.student_management.security.service;

import com.tpe.student_management.entity.user.User;
import com.tpe.student_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //! Note: If "login" is used instead of "username" as the subject in the token claims
        //! login can be supported via both email and username.
        //! Repository method would be: findByUsernameOrEmail

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("No user found with given username: " + username)
        );

        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setId(user.getId());
        userDetails.setUsername(user.getUsername());
        userDetails.setPassword(user.getPassword());
        userDetails.setGrantedAuthorities(Set.of(new SimpleGrantedAuthority(user.getUserRole().getRole().name())));

        return userDetails;
    }
}
