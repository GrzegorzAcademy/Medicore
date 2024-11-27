package pl.infoshare.clinicweb.doctor;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DoctorDto {

    private Long id;
    @NotBlank(message = "Pole nie może być puste")
    private String name;
    @NotBlank(message = "Pole nie może być puste")
    private String surname;
    @NotBlank(message = "Pole nie może być puste")
    private Specialization specialization;

}
