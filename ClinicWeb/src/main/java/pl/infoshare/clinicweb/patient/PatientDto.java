package pl.infoshare.clinicweb.patient;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.infoshare.clinicweb.user.entity.Gender;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PatientDto {

    private Long id;
    @NotBlank(message = "Pole nie może być puste")
    private String name;
    @NotBlank(message = "Pole nie może być puste")
    private String surname;
    @NotBlank(message = "Pole nie może być puste")
    private String phoneNumber;
    @NotBlank(message = "Pole nie może być puste")
    private String pesel;
    private String country;
    private String street;
    private String city;
    private String zipCode;
    private String houseNumber;
    private String flatNumber;
    private Gender gender;


}
