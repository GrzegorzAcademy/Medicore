package pl.infoshare.clinicweb.doctor;

import lombok.Builder;
import org.springframework.stereotype.Component;
import pl.infoshare.clinicweb.user.entity.PersonDetails;


@Component
@Builder
public class DoctorMapper {

    public DoctorDto toDto(Doctor doctor) {
        return DoctorDto.builder()
                .name(doctor.getDetails().getName())
                .id(doctor.getId())
                .surname(doctor.getDetails().getSurname())
                .specialization(doctor.getSpecialization())
                .build();

    }


    public Doctor toEntity(DoctorDto doctorDto) {
        return Doctor.builder()
                .id(doctorDto.getId())
                .details(PersonDetails.builder()
                        .name(doctorDto.getName())
                        .surname(doctorDto.getSurname())
                        .build())
                .specialization(doctorDto.getSpecialization())
                .build();
    }
}
