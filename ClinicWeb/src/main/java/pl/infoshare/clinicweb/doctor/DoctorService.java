package pl.infoshare.clinicweb.doctor;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import pl.infoshare.clinicweb.patient.Address;
import pl.infoshare.clinicweb.user.entity.PersonDetails;

import java.util.List;

@Service
@AllArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    public void addDoctor(Doctor user) {
        doctorRepository.save(user);
    }

    public DoctorDto findById(long id) {
        return doctorRepository
                .findById(id)
                .map(doctorMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Doctor not found with ID: %s", id)));

    }

    public List<DoctorDto> findAllDoctors() {

        return doctorRepository.findAll()
                .stream()
                .map(doctorMapper::toDto)
                .toList();
    }

    public Page<DoctorDto> findAllPage(int pageNumber) {

        final int pageSize = 10;
        Page<DoctorDto> doctors;

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id"));
        Page<Doctor> entities = doctorRepository.findAll(pageable);


        return entities.map(doctorMapper::toDto);

    }


    public void deleteDoctor(Long idDoctor) {

        doctorRepository.deleteById(idDoctor);
    }

    public void updateDoctor(DoctorDto doctorDto, Address address) {

        Doctor doctor = doctorMapper.toEntity(doctorDto);
        doctor.setAddress(address);

        doctorRepository.save(doctor);
    }


    Page<DoctorDto> findDoctorBySpecialization(int pageNumber, Specialization specialization) {

        final int pageSize = 10;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id"));

        List<DoctorDto> doctorDtos = doctorRepository.findAllBySpecialization(specialization)
                .stream()
                .map(doctorMapper::toDto)
                .toList();


        return new PageImpl<>(doctorDtos);
    }

    public void setDoctorAttributes(Doctor doctor, PersonDetails personDetails,
                                    Address address, Specialization specialization) {

        doctor.setDetails(personDetails);
        doctor.setAddress(address);
        doctor.setSpecialization(specialization);


    }

    public boolean existsByPesel(String pesel) {

        return doctorRepository.findByPesel(pesel).isEmpty() ? false : true;
    }


}
