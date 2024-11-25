package pl.infoshare.clinicweb.patient;

import org.springframework.stereotype.Component;
import pl.infoshare.clinicweb.user.entity.PersonDetails;

@Component
public class PatientMapper {

    public PatientDto toDto(Patient patient) {

        PatientDto patientDto = new PatientDto();

        patientDto.setId(patient.getId());
        patientDto.setName(patient.getPersonDetails().getName());
        patientDto.setSurname(patient.getPersonDetails().getSurname());
        patientDto.setPesel(patient.getPersonDetails().getPesel());
        patientDto.setPhoneNumber(patient.getPersonDetails().getPhoneNumber());
        patientDto.setCountry(patient.getAddress().getCountry());
        patientDto.setCity(patient.getAddress().getCity());
        patientDto.setZipCode(patient.getAddress().getZipCode());
        patientDto.setFlatNumber(patient.getAddress().getFlatNumber());
        patientDto.setHouseNumber(patient.getAddress().getHouseNumber());
        patientDto.setStreet(patient.getAddress().getStreet());

        return patientDto;

    }

    public Patient toEntity(PatientDto patientDto) {
        Patient patient = new Patient();


        if (patient.getPersonDetails() == null) {
            patient.setPersonDetails(new PersonDetails());
        }

        if (patient.getAddress() == null) {
            patient.setAddress(new Address());
        }

        patient.setId(patientDto.getId());
        patient.getPersonDetails().setName(patientDto.getName());
        patient.getPersonDetails().setSurname(patientDto.getSurname());
        patient.getPersonDetails().setPesel(patientDto.getPesel());
        patient.getPersonDetails().setPhoneNumber(patientDto.getPhoneNumber());

        patient.getAddress().setCountry(patientDto.getCountry());
        patient.getAddress().setCity(patientDto.getCity());
        patient.getAddress().setZipCode(patientDto.getZipCode());
        patient.getAddress().setFlatNumber(patientDto.getFlatNumber());
        patient.getAddress().setHouseNumber(patientDto.getHouseNumber());
        patient.getAddress().setStreet(patientDto.getStreet());

        return patient;
    }
}
