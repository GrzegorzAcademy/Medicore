package pl.infoshare.clinicweb.patient;

import jakarta.persistence.*;
import lombok.Data;
import pl.infoshare.clinicweb.clinic.Clinic;
import pl.infoshare.clinicweb.doctor.Doctor;
import pl.infoshare.clinicweb.patientCard.PatientCard;
import pl.infoshare.clinicweb.user.entity.PersonDetails;
import pl.infoshare.clinicweb.user.entity.User;

import java.util.HashSet;
import java.util.Set;


@Data
@Entity
@Table(name = "patients")
public class Patient {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private PersonDetails personDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PatientCard> patientCards = new HashSet<>();

    @ManyToOne
    private Clinic clinic;

    @Embedded
    private Address address;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;
}
