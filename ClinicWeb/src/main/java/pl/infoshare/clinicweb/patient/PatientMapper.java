package pl.infoshare.clinicweb.patient;

import org.springframework.stereotype.Component;
import pl.infoshare.clinicweb.clinic.Clinic;
import pl.infoshare.clinicweb.user.entity.PersonDetails;

@Component
public class PatientMapper {

    public PatientDto toDto(Patient patient) {
        return PatientDto.builder()
                .id(patient.getId())
                .name(patient.getPersonDetails().getName())
                .surname(patient.getPersonDetails().getSurname())
                .pesel(patient.getPersonDetails().getPesel())
                .phoneNumber(patient.getPersonDetails().getPhoneNumber())
                .country(patient.getAddress().getCountry())
                .city(patient.getAddress().getCity())
                .zipCode(patient.getAddress().getZipCode())
                .flatNumber(patient.getAddress().getFlatNumber())
                .houseNumber(patient.getAddress().getHouseNumber())
                .street(patient.getAddress().getStreet())
                .build();

    }

    public Patient toEntity(PatientDto patientDto) {
        return Patient.builder()
                .id(patientDto.getId())
                .personDetails(PersonDetails.builder()
                        .name(patientDto.getName())
                        .surname(patientDto.getSurname())
                        .pesel(patientDto.getPesel())
                        .phoneNumber(patientDto.getPhoneNumber())
                        .build())
                .address(Address.builder()
                        .country(patientDto.getCountry())
                        .city(patientDto.getCity())
                        .zipCode(patientDto.getZipCode())
                        .flatNumber(patientDto.getFlatNumber())
                        .houseNumber(patientDto.getHouseNumber())
                        .street(patientDto.getStreet())
                        .build())
                .build();
    }
}
