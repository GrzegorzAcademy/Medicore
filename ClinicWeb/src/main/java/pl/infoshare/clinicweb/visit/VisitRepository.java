package pl.infoshare.clinicweb.visit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    @Query("SELECT v FROM Visit v WHERE v.patient.id = :patientId")
    List<Visit> findAllByPatientId(Long patientId);


    @Query("SELECT count(v)  FROM Visit v WHERE v.doctor.id = :doctor AND v.visitTime BETWEEN :startTime AND :endTime")
    int findByDoctorAndVisitTimeBetween(long doctor, LocalDateTime startTime, LocalDateTime endTime);
}
