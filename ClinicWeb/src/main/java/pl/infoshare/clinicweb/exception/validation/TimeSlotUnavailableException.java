package pl.infoshare.clinicweb.exception.validation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class TimeSlotUnavailableException extends RuntimeException {

    private static final String MSG = "Wybrany termin jest już zajęty: %s.";

    public TimeSlotUnavailableException(LocalDateTime localDateTime) {
        super(String.format(MSG, localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
    }
}