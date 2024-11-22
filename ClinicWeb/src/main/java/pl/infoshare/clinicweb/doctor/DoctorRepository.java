package pl.infoshare.clinicweb.doctor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query("select d from Doctor d where d.details.pesel=:pesel")
    Optional<Doctor> findByPesel(String pesel);

    @Query("select d from Doctor d where d.specialization=:specialization")
    List<Doctor> findAllBySpecialization(Specialization specialization);


}
