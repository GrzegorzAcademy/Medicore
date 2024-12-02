package pl.infoshare.clinicweb.doctor;

import lombok.Builder;
import org.springframework.stereotype.Component;
import pl.infoshare.clinicweb.patient.Address;
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
                .phoneNumber(doctor.getDetails().getPhoneNumber())
                .pesel(doctor.getDetails().getPesel())
                .country(doctor.getAddress().getCountry())
                .city(doctor.getAddress().getCity())
                .houseNumber(doctor.getAddress().getHouseNumber())
                .street(doctor.getAddress().getStreet())
                .flatNumber(doctor.getAddress().getFlatNumber())
                .zipCode(doctor.getAddress().getZipCode())
                .gender(doctor.getDetails().getGender())
                .build();

    }


    public Doctor toEntity(DoctorDto doctorDto) {
        return Doctor.builder()
                .id(doctorDto.getId())
                .details(PersonDetails.builder()
                        .name(doctorDto.getName())
                        .surname(doctorDto.getSurname())
                        .phoneNumber(doctorDto.getPhoneNumber())
                        .pesel(doctorDto.getPesel())
                        .gender(doctorDto.getGender())
                        .build())
                .address(Address.builder()
                        .country(doctorDto.getCountry())
                        .city(doctorDto.getCity())
                        .flatNumber(doctorDto.getFlatNumber())
                        .houseNumber(doctorDto.getHouseNumber())
                        .street(doctorDto.getStreet())
                        .zipCode(doctorDto.getZipCode())
                        .build())
                .specialization(doctorDto.getSpecialization())
                .build();
    }
}
