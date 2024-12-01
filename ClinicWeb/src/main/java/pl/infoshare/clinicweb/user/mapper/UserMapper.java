package pl.infoshare.clinicweb.user.mapper;

import org.springframework.stereotype.Component;
import pl.infoshare.clinicweb.doctor.Doctor;
import pl.infoshare.clinicweb.patient.Patient;
import pl.infoshare.clinicweb.user.entity.User;
import pl.infoshare.clinicweb.user.registration.UserDto;

@Component
public class UserMapper {

    public UserDto toDto(User user) {

        var userDto = new UserDto();

        switch(user.getRole()) {

            case  PATIENT:

                userDto.setPatientId(user.getPatient().getId());
                break;

            case DOCTOR:
                userDto.setDoctorId(user.getDoctor().getId());
                break;
        }

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setSurname(user.getSurname());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        userDto.setPassword(user.getPassword());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setPesel(user.getPesel());

        return userDto;

    }

    public User toEntity(UserDto userDto) {

        var user = new User();

        switch(userDto.getRole()) {

            case  PATIENT:

                var patient = new Patient();
                patient.setId(userDto.getPatientId());
                user.setPatient(patient);
                break;

            case DOCTOR:
                var doctor = new Doctor();
                doctor.setId(userDto.getDoctorId());
                user.setDoctor(doctor);
                break;
        }

        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());
        user.setPassword(userDto.getPassword());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setPesel(userDto.getPesel());

        return user;

    }
}
