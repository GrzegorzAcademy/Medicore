package pl.infoshare.clinicweb.visit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.infoshare.clinicweb.doctor.Specialization;

import java.time.LocalDateTime;

@Data
public class VisitDto {

    private Long id;
    private LocalDateTime visitDate;
    private String patientName;
    private String patientSurname;
    private String patientPhoneNumber;
    private String patientPesel;
    private String doctorName;
    private String doctorSurname;
    private Specialization doctorSpecialization;
    private boolean visitCancelled;
    private Long patientId;
    private long doctorId;
    private boolean isVisitPastDate;

}
