package pl.infoshare.clinicweb.user.registration;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import pl.infoshare.clinicweb.annotation.peselDuplicate.PeselDuplicateValidator;
import pl.infoshare.clinicweb.emailAnnotation.EmailMatcherValidator;
import pl.infoshare.clinicweb.passwordAnnotation.PasswordMatcherValidator;
import pl.infoshare.clinicweb.user.entity.Role;


@Data
@PasswordMatcherValidator
@EmailMatcherValidator
@Builder
public class UserDto {

    private Long id;
    @NotEmpty(message = "Pole email nie może być puste.")
    @Email(message = "Niepoprawny format, poprawny format e-mail np. xxxx@xxx.xx")
    private String email;
    @NotEmpty(message = "Pole hasło nie może być puste.")
    @Size(min = 6, message = "Hasło musi składać się z przynajmniej 6 znaków.")
    private String password;
    @NotEmpty(message = "Pole nie może być puste.")
    @Size(min = 6, message = "Hasło musi składać się z przynajmniej 6 znaków.")
    private String confirmPassword;
    @NotNull(message = "Podaj swoją rolę użytkownika:")
    private Role role;
    @NotEmpty(message = "Pole nie może być puste.")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Pole musi składać się z samych liter")
    private String name;
    @NotEmpty(message = "Pole nie może być puste.")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Pole musi składać się z samych liter")
    private String surname;
    @NotEmpty(message = "Pole nie może być puste")
    @Pattern(regexp = "[0-9]{11}", message = "Pole musi zawierać 11 cyfr. ")
    @PeselDuplicateValidator
    private String pesel;
    @NotEmpty(message = "Pole nie może być puste")
    @Pattern(regexp = "^\\d{9}$", message = "Pole musi składać się z 9 cyfr.")
    private String phoneNumber;
    private Long doctorId;
    private Long patientId;
}
