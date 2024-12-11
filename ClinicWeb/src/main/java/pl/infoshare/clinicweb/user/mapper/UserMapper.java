package pl.infoshare.clinicweb.user.mapper;

import org.springframework.stereotype.Component;
import pl.infoshare.clinicweb.user.entity.User;
import pl.infoshare.clinicweb.user.registration.UserDto;

@Component
public class UserMapper {

    public UserDto toDto(User user) {

        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .password(user.getPassword())
                .build();


    }

    public User toEntity(UserDto userDto) {

        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .role(userDto.getRole())
                .password(userDto.getPassword())
                .build();

    }
}
