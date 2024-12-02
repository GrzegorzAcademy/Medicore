package pl.infoshare.clinicweb.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.infoshare.clinicweb.user.entity.User;
import pl.infoshare.clinicweb.user.mapper.UserMapper;
import pl.infoshare.clinicweb.user.registration.UserDto;
import pl.infoshare.clinicweb.user.repository.UserRepository;

import java.util.Optional;

import static java.lang.String.format;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = findUserByEmail(email);
        log.info("Logged in user : {} ", user.getEmail());
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(String.valueOf(user.getRole()))
                .build();

    }

    public void addUser(User user) {

        userRepository.save(user);
    }

    public void saveUser(UserDto user) {

        var appUser = userMapper.toEntity(user);
        userRepository.save(appUser);
        log.info("User patient saved with ID: {}", appUser.getId());
    }

    public void deleteUserById(Long id) {

        userRepository.deleteById(id);
    }

    public User findUserByEmail(final String email) {

        return userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException(format("User not found with email: %s", email)));

    }

    public boolean isUserAlreadyRegistered(String email) {

        Optional<User> user = userRepository.findUserByEmail(email);

        return user.isPresent();

    }


}
