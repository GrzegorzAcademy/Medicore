package pl.infoshare.clinicweb.annotation.peselDuplicate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.infoshare.clinicweb.doctor.DoctorService;
import pl.infoshare.clinicweb.patient.PatientService;

@Component
@RequiredArgsConstructor
public final class UniquePesel implements ConstraintValidator<UniquePeselValidator, String> {


    private final PatientService patientService;
    private final DoctorService doctorService;


    @Override
    public void initialize(UniquePeselValidator constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String pesel, ConstraintValidatorContext constraintValidatorContext) {

        if (pesel == null || pesel.isEmpty()) {
            return true;
        }

        return patientService.existsByPesel(pesel) || doctorService.existsByPesel(pesel) ? false : true;
    }
}

