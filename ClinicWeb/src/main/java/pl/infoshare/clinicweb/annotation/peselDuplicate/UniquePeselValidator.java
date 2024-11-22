package pl.infoshare.clinicweb.annotation.peselDuplicate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniquePesel.class)
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniquePeselValidator {
    String message() default "Podany numer pesel jest już zajęty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}