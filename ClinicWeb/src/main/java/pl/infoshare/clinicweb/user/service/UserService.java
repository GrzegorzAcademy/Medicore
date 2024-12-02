package pl.infoshare.clinicweb.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.infoshare.clinicweb.doctor.Doctor;
import pl.infoshare.clinicweb.doctor.DoctorService;
import pl.infoshare.clinicweb.doctor.Specialization;
import pl.infoshare.clinicweb.patient.Address;
import pl.infoshare.clinicweb.patient.Patient;
import pl.infoshare.clinicweb.patient.PatientService;
import pl.infoshare.clinicweb.user.entity.PersonDetails;
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
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = findUserByEmail(email);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(String.valueOf(user.getRole()))
                .build();
    }

    public void addUser(User user) {

        userRepository.save(user);
    }


    @Transactional
    public void saveUser(UserDto user) {

        var appUser = userMapper.toEntity(user);

        var personDetails = new PersonDetails();
        var address = new Address();
        address.setCountry("Polska");


        switch (user.getRole()) {

            case PATIENT:
                var patient = new Patient();

                patient.setAddress(address);

                patient.setPersonDetails(personDetails);
                personDetails.setName(user.getName());
                personDetails.setSurname(user.getSurname());
                personDetails.setPesel(user.getPesel());
                personDetails.setPhoneNumber(user.getPhoneNumber());

                appUser.setPatient(patient);
                patientService.addPatient(patient);

                patient.setUser(appUser);
                break;

            case DOCTOR:

                var doctor = new Doctor();
                doctor.setAddress(address);
                doctor.setSpecialization(Specialization.NOT_CHOSEN);

                doctor.setDetails(personDetails);
                personDetails.setName(user.getName());
                personDetails.setSurname(user.getSurname());
                personDetails.setPesel(user.getPesel());
                personDetails.setPhoneNumber(user.getPhoneNumber());

                doctorService.addDoctor(doctor);
                appUser.setDoctor(doctor);
                doctor.setUser(appUser);

                break;

        }

        userRepository.save(appUser);

        log.info("User patient saved with Email: {}", appUser.getEmail());
    }

    public void deleteUserById(Long id) {

        userRepository.deleteById(id);
    }

    public User findUserByEmail(final String email) {

        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(format("User not found with email: %s", email)));

    }

    public boolean isUserAlreadyRegistered(String email) {

        Optional<User> user = userRepository.findUserByEmail(email);

        return user.isPresent();

    }

    public UserDto getLoggedInUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findUserByEmail(email)
                .map(userMapper::toDto)
                .orElseThrow(() -> new RuntimeException("User was not found"));
    }


}
