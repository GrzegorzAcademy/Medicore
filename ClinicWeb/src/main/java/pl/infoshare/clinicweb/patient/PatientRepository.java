package pl.infoshare.clinicweb.patient;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("select p from Patient p where p.personDetails.pesel=:pesel")
    Optional<Patient> findByPesel(String pesel);

    @Query("select case when count(p) > 0 then true else false end from Patient p where p.personDetails.pesel=:pesel")
    boolean existsByPesel(String pesel);


}
