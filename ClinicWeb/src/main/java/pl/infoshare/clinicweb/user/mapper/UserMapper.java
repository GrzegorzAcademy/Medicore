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
                .password(user.getPassword())
                .role(user.getRole())
                .detailsFormFilled(user.isDetailsFormFilled())
                .build();
    }

    public User toEntity(UserDto userDto) {

        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .role(userDto.getRole())
                .detailsFormFilled(userDto.isDetailsFormFilled())
                .build();
    }
}
