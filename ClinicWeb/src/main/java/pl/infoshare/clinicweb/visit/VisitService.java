package pl.infoshare.clinicweb.visit;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.infoshare.clinicweb.doctor.Doctor;
import pl.infoshare.clinicweb.doctor.DoctorDto;
import pl.infoshare.clinicweb.doctor.DoctorMapper;
import pl.infoshare.clinicweb.doctor.DoctorService;
import pl.infoshare.clinicweb.exception.validation.TimeSlotUnavailableException;
import pl.infoshare.clinicweb.patient.Patient;
import pl.infoshare.clinicweb.patient.PatientDto;
import pl.infoshare.clinicweb.patient.PatientMapper;
import pl.infoshare.clinicweb.patient.PatientService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class VisitService {

    private final DoctorService doctorService;
    private final PatientService patientService;
    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;
    private final DoctorMapper doctorMapper;
    private final PatientMapper patientMapper;

    public void saveVisit(Visit visit, Long doctorId, Long patientId, LocalDateTime visitTime) {
        if (isTimeSlotAvailable(doctorId, visitTime)) {
            throw new TimeSlotUnavailableException(visitTime);

        }

        DoctorDto doctor = doctorService.findById(doctorId);
        Doctor entityDoctor = doctorMapper.toEntity(doctor);


        PatientDto patient = patientService.findById(patientId);
        Patient entityPatient = patientMapper.toEntity(patient);

        visit.setDoctor(entityDoctor);
        visit.setPatient(entityPatient);
        visit.setVisitTime(visitTime);
        visitRepository.save(visit);
    }

    public List<VisitDto> findAllVisits() {

        return visitRepository.findAll()
                .stream()
                .map(visitMapper::toVisitDto)
                .toList();
    }

    public Page<VisitDto> findPage(int pageNumber) {

        final int pageSize = 10;

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.DESC, "visitDate"));
        Page<Visit> entities = visitRepository.findAll(pageable);

        Page<VisitDto> visits = entities.map(visit -> {
            VisitDto visitDto = visitMapper.toVisitDto(visit);

            return visitDto;
        });

        return visits;
    }

    public void cancelVisit(VisitDto visitDto) {

        if (visitDto.isVisitPastDate() || !visitDto.isVisitCancelled()) {

            Visit visit = visitMapper.toEntity(visitDto);

            visit.setCancelVisit(true);

            visitRepository.save(visit);
        }

    }


    public VisitDto findVisitById(Long id) {

        return visitRepository
                .findById(id)
                .map(visitMapper::toVisitDto)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("Visit not found with given ID: %d", id)));
    }

    static List<LocalDateTime> generateAvailableTimes() {
        List<LocalDateTime> availableTimes = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusMinutes(30);

        LocalDate startDay = now.toLocalDate();
        LocalDate endOfMonth = now.with(TemporalAdjusters.lastDayOfMonth()).toLocalDate();

        for (LocalDate current = startDay; !current.isAfter(endOfMonth); current = current.plusDays(1)) {
            LocalDateTime startTime;
            LocalDateTime endTime = current.atTime(18, 0);

            if (current.isEqual(now.toLocalDate())) {
                startTime = now.isBefore(current.atTime(8, 0)) ? current.atTime(8, 0) : now.plusMinutes(30);
            } else {
                startTime = current.atTime(8, 0);
            }

            while (startTime.isBefore(endTime)) {
                availableTimes.add(startTime);
                startTime = startTime.plusMinutes(30);
            }
        }

        return availableTimes;
    }

    public boolean isTimeSlotAvailable(Long doctorId, LocalDateTime visitTime) {
        LocalDateTime endTime = calculateEndTime(visitTime);
        return !areVisitsAvailable(doctorId, visitTime, endTime);
    }

    private LocalDateTime calculateEndTime(LocalDateTime visitTime) {
        return visitTime.plusMinutes(30);
    }

    private boolean areVisitsAvailable(long id, LocalDateTime startTime, LocalDateTime endTime) {
        int countVisit = visitRepository.findByDoctorAndVisitTimeBetween(id, startTime, endTime);
        return countVisit < 1;

    }

}