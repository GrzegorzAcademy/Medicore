package pl.infoshare.clinicweb.patient;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.infoshare.clinicweb.patientCard.PatientCardRepository;
import pl.infoshare.clinicweb.user.entity.PersonDetails;
import pl.infoshare.clinicweb.visit.Visit;
import pl.infoshare.clinicweb.visit.VisitRepository;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final VisitRepository visitRepository;
    private final PatientCardRepository patientCardRepository;


    public void addPatient(Patient patient) {
        log.info("Adding new patient: {}", patient.getId());
        patientRepository.save(patient);
    }

    public PatientDto findById(Long id) {
        log.info("Searching for patient by id: {}", id);
        return patientRepository.findById(id)
                .map(patientMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Patient not found with id %s", id)));
    }

    public PatientDto findByPesel(String pesel) {
        log.info("Searching for patient by PESEL: {}", pesel);
        return patientRepository.findByPesel(pesel.trim())
                .map(patientMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Patient not found with pesel %s", pesel)));
    }

    public boolean existsByPesel(String pesel) {
        log.info("Checking existence of patient by PESEL: {}", pesel);
        boolean exists = patientRepository.findByPesel(pesel).isPresent();
        log.info("Patient exists by PESEL {}: {}", pesel, exists);
        return exists;
    }

    public List<PatientDto> findAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(patientMapper::toDto)
                .toList();
    }

    public Page<PatientDto> findPage(int pageNumber) {

        final int pageSize = 10;

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id"));
        Page<Patient> entities = patientRepository.findAll(pageable);

        return entities.map(patientMapper::toDto);
    }

    public void updatePatient(PatientDto patientDto, Address address, PersonDetails personDetails) {

        Patient patient = patientRepository.findById(patientDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Pacjent o ID " + patientDto.getId() + " nie istnieje."));
        patient.setAddress(address);
        patient.setPersonDetails(personDetails);


        patientRepository.save(patient);
    }


    @Transactional
    public void deletePatient(Long id) {
        List<Visit> visits = visitRepository.findAllByPatientId(id);
        if (!visits.isEmpty()) {
            visitRepository.deleteAll(visits);
        }
        patientCardRepository.findById(id).ifPresent(patientCardRepository::delete);
        patientRepository.findById(id).ifPresent(patientRepository::delete);
    }

    public void setPatientAttributes(Patient patient, PersonDetails personDetails,
                                     Address address) {

        LocalDate dateBirthDate = PatientService.decodeDateOfBirth(personDetails.getPesel());

        patient.setPersonDetails(personDetails);
        patient.getPersonDetails().setBirthDate(dateBirthDate);
        patient.setAddress(address);

    }

    public static LocalDate decodeDateOfBirth(String pesel) {

        String yearPart = pesel.substring(0, 2);
        String monthPart = pesel.substring(2, 4);
        int day = Integer.parseInt(pesel.substring(4, 6));

        String decodedYearPart;
        int monthValue;

        int month = Integer.parseInt(monthPart);
        if (month >= 81 && month <= 92) {
            decodedYearPart = "18";
            monthValue = month - 80;
        } else if (month >= 1 && month <= 12) {
            decodedYearPart = "19";
            monthValue = month;
        } else if (month >= 21 && month <= 32) {
            decodedYearPart = "20";
            monthValue = month - 20;
        } else if (month >= 41 && month <= 52) {
            decodedYearPart = "21";
            monthValue = month - 40;
        } else if (month >= 61 && month <= 72) {
            decodedYearPart = "22";
            monthValue = month - 60;
        } else {
            throw new IllegalArgumentException("Invalid month in PESEL");
        }

        int fullYear = Integer.parseInt(decodedYearPart + yearPart);

        LocalDate dateOfBirth;
        try {
            dateOfBirth = LocalDate.of(fullYear, monthValue, day);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("Invalid date of birth from PESEL");
        }

        return dateOfBirth;
    }

}