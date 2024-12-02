package pl.infoshare.clinicweb.doctor;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import pl.infoshare.clinicweb.user.entity.Gender;

import java.util.PrimitiveIterator;

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
    @NotBlank(message = "Pole nie może być puste")
    private String phoneNumber;
    private String pesel;
    private String country;
    private String street;
    private String city;
    private String zipCode;
    private String houseNumber;
    private String flatNumber;
    private Gender gender;


}
