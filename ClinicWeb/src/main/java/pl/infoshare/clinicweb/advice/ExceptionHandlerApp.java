package pl.infoshare.clinicweb.advice;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.infoshare.clinicweb.exception.validation.TimeSlotUnavailableException;


import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
class ExceptionHandlerApp {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleException(MethodArgumentNotValidException exception) {
        Map<String, String> errorsMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach((error) -> {

            errorsMap.put(error.getField(), error.getDefaultMessage());

        });
        return errorsMap;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleException(EntityNotFoundException exception, Model model) {

        model.addAttribute("error", "Nie znaleziono podanego obiektu w bazie.");

        log.info("Entity not found exception was thrown.");

        return "patient/search-patient";
    }

    @ExceptionHandler(PeselFormatException.class)
    public String handleException(PeselFormatException exception, Model model) {

        model.addAttribute("error", "Niepoprawny format numeru pesel.");

        log.info("Incorrect pesel format exception was thrown.");

        return "patient/search-patient";
    }

    @ExceptionHandler(TimeSlotUnavailableException.class)
    public String handleTimeSlotUnavailableException(TimeSlotUnavailableException exception, Model model) {

        model.addAttribute("error", "Wybrany termin jest juz zajety");
        return "visit/visit";

    }

    @ExceptionHandler(UserEmailExistsException.class)
    public String handleException(UserEmailExistsException exception, Model model) {

        model.addAttribute("error", "Użytkownik o podanym adresie email już istnieje.");
        return "user/registry";
    }


}
