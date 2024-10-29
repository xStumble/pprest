package me.xstumble.ppcrudbootsecurity.services;

import me.xstumble.ppcrudbootsecurity.models.User;
import me.xstumble.ppcrudbootsecurity.repositories.UserRepository;
import me.xstumble.ppcrudbootsecurity.security.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }

        return new UserDetailsImpl(user.get());
    }

}
