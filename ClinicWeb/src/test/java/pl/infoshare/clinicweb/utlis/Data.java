package pl.infoshare.clinicweb.utlis;

import pl.infoshare.clinicweb.user.entity.Role;
import pl.infoshare.clinicweb.user.entity.User;
import pl.infoshare.clinicweb.user.registration.UserDto;

public class Data {

    public static UserDto createUserDTO(final String email, final String password) {

        return UserDto.builder()
                .id(1L)
                .email(email)
                .password(password)
                .role(Role.PATIENT)
                .build();

    }

    public static User createUser(final String email, final String password) {

        return User.builder()
                .id(1L)
                .email(email)
                .password(password)
                .role(Role.PATIENT)
                .build();

    }
}
