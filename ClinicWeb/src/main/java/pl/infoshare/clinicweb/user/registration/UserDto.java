package pl.infoshare.clinicweb.user.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
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
    private Long doctorId;
    private Long patientId;
    private boolean verified;
    private boolean formFilled;

}
