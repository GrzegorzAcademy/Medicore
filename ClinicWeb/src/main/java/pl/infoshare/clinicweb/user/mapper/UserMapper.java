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
                .name(user.getName())
                .surname(user.getSurname())
                .pesel(user.getPesel())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .doctorId(user.getDoctor().getId())
                .patientId(user.getPatient().getId())
                .build();
    }

    public User toEntity(UserDto userDto) {

        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .pesel(userDto.getPesel())
                .phoneNumber(userDto.getPhoneNumber())
                .role(userDto.getRole())
                .build();
    }
}
