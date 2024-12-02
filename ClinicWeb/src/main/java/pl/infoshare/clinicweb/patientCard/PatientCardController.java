package pl.infoshare.clinicweb.patientCard;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.infoshare.clinicweb.visit.VisitDto;
import pl.infoshare.clinicweb.visit.VisitService;

import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
@AllArgsConstructor
public class PatientCardController {


    private final PatientCardService patientCardService;
    private final VisitService visitService;
    private final PatientCardMapper patientCardMapper;

    @GetMapping("/patient-card")
    public String createPatientCard(@RequestParam(value = "id", required = false) Long id, Model model) {
        log.info("createPatientCard method invoked for visit with ID: {}", id);

        if (id == null) {
            log.warn("Visit ID not provided.");
            model.addAttribute("error", "Nie znaleziono identyfikatora wizyty.");
            return "error";
        }

        VisitDto visit = visitService.findVisitById(id);
        if (visit == null) {
            log.warn("Nie znaleziono wizyty o id: {}", id);
            model.addAttribute("error", "Wizyta nie istnieje.");
            return "error";
        }

        PatientCardDTO patientCardDTO = PatientCardService.getPatientCardDTO(visit);
        model.addAttribute("visit", visit);
        model.addAttribute("patientCard", patientCardDTO);

        log.info("Finished processing createPatientCard for visit with ID: {}", id);
        return "patient/patient-card";
    }


    @GetMapping("/detail-patient-appointments")
    public String getDetailPatientAppointments(@RequestParam(value = "id", required = false) Long patientId, Model model) {
        log.info("getDetailPatientAppointments method invoked for patient with ID: {}", patientId);

        if (patientId == null) {
            log.warn("Patient ID not provided.");
            model.addAttribute("error", "Nie podano identyfikatora pacjenta.");
            return "error";
        }


        List<PatientCard> patientAppointments = patientCardService.findAllPatientCardByPatientId(patientId);
        if (patientAppointments == null || patientAppointments.isEmpty()) {
            log.warn("No appointments found for patient with ID: {}", patientId);
            model.addAttribute("error", "Pacjent nie ma zapisanych wizyt.");
            return "error";
        }


        PatientCardDTO matchingPatientCard = patientCardService.findById(patientId);
        if (matchingPatientCard == null) {
            log.warn("No detailed patient card found with ID: {}", patientId);
            model.addAttribute("error", "Nie znaleziono szczegółowej karty pacjenta.");
            return "error";
        }


        model.addAttribute("matchingPatientCard", matchingPatientCard);
        model.addAttribute("patientAppointments", patientAppointments);

        log.info("Finished processing getDetailPatientAppointments for patient with ID: {}", patientId);
        return "patient/detail-patient-appointments";
    }


    @GetMapping("/patient-appointments")
    public String getPatientAppointments(@RequestParam(value = "id", required = false) Long patientId, Model model) {
        log.info("getPatientAppointments method invoked for patient with ID: {}", patientId);

        if (patientId == null) {
            log.warn("Patient ID not provided.");
            model.addAttribute("error", "Nie podano identyfikatora pacjenta.");
            return "error";
        }


        List<PatientCard> patientAppointments = patientCardService.findAllPatientCardByPatientId(patientId);
        if (patientAppointments == null || patientAppointments.isEmpty()) {
            log.info("No appointments found for patient with ID: {}", patientId);
            model.addAttribute("message", "Pacjent nie ma zapisanych wizyt.");
            return "patient/patient-appointments";
        }

        model.addAttribute("patientAppointments", patientAppointments);
        log.info("Found {} appointments for patient with ID: {}", patientAppointments.size(), patientId);

        return "patient/patient-appointments";
    }
        @PostMapping("/patient-card")
        public String savePatientCard(
                @Valid @ModelAttribute("patientCard") PatientCardDTO patientCardDTO,
                BindingResult bindingResult,
                RedirectAttributes redirectAttributes,
                Model model) {

            if (bindingResult.hasErrors()) {
                log.error("validation error patient card saved: {}", bindingResult.getAllErrors());
                model.addAttribute("error", "Błąd zapisu karty pacjenta");
                return "patient/patient-card";
            }

            try {
                PatientCardDTO matchingPatientCard = patientCardService.findById(patientCardDTO.getPatientId());
                PatientCard patientCard = patientCardMapper.toEntity(patientCardDTO);
                model.addAttribute("matchingCard",matchingPatientCard);

                patientCardService.patientCardSave(patientCard);
                log.info("Patient card success saved {}", patientCard.getId());
                redirectAttributes.addFlashAttribute("successMessage", "Karta pacjenta została pomyślnie zapisana!");
                return "patient/patient-appointments";

            } catch (Exception e) {
                log.error("error patient card saved", e);
                model.addAttribute("error", "Wystąpił nieoczekiwany błąd podczas zapisu karty pacjenta");
                return "patient/patient-card";
            }
        }
    }


