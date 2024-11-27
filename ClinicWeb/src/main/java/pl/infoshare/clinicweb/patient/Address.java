package pl.infoshare.clinicweb.patient;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @NotEmpty(message = "Pole nie może być puste")
    private String city;
    @NotEmpty(message = "Pole nie może być puste")
    private String country;
    @NotEmpty(message = "Pole nie może być puste")
    private String zipCode;
    @NotEmpty(message = "Pole nie może być puste")
    private String street;
    @NotEmpty(message = "Pole nie może być puste")
    @Pattern(regexp = "[0-9]{1,6}", message = "Pole musi zawierać same cyfry. ")
    private String houseNumber;
    @Pattern(regexp = "[0-9]{0,6}", message = "Pole musi zawierać same cyfry. ")
    private String flatNumber;
}
