package pl.infoshare.clinicweb.visit;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.infoshare.clinicweb.doctor.Doctor;
import pl.infoshare.clinicweb.doctor.DoctorDto;
import pl.infoshare.clinicweb.doctor.DoctorService;
import pl.infoshare.clinicweb.exception.validation.TimeSlotUnavailableException;
import pl.infoshare.clinicweb.patient.Patient;
import pl.infoshare.clinicweb.patient.PatientDto;
import pl.infoshare.clinicweb.patient.PatientService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
public class VisitController {

    private final VisitService visitService;
    private final DoctorService doctorService;
    private final PatientService patientService;


    public VisitController(VisitService visitService, DoctorService doctorService, PatientService patientService) {

        this.visitService = visitService;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @GetMapping("/visit")
    public String showVisitForm(Model model) {

        return prepareVisitFormData(model);
    }

    @PostMapping("/visit")
    public String visitFormSubmission(
            @Valid @ModelAttribute("visit") Visit visit,
            BindingResult visitBindingResult,
            @RequestParam(value = "patientId", required = false) Long patientId,
            @RequestParam(value = "doctorId", required = false) Long doctorId,
            @RequestParam(value = "visitDate") String visitDate,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (visitBindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Wystąpiły błędy w formularzu. Proszę poprawić poniższe błędy.");
            prepareVisitFormData(model);
            return "visit/visit";
        }

        LocalDateTime visitDateTime;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            visitDateTime = LocalDateTime.parse(visitDate, formatter);
        } catch (DateTimeParseException e) {
            model.addAttribute("errorMessage", "Nieprawidłowy format daty. Proszę wybrać poprawną datę i godzinę.");
            prepareVisitFormData(model);
            return "visit/visit";
        }

        visit.setVisitTime(visitDateTime);


        try {
            if (visitService.isTimeSlotAvailable(doctorId, visitDateTime)) {
                throw new TimeSlotUnavailableException(visitDateTime);
            }
        } catch (TimeSlotUnavailableException e) {
            model.addAttribute("errorMessage", e.getMessage());
            prepareVisitFormData(model);
            return "visit/visit";
        }

        if (doctorId != null) {
            Doctor doctor = new Doctor();
            doctor.setId(doctorId);
            visit.setDoctor(doctor);
        }
        if (patientId != null) {
            Patient patient = new Patient();
            patient.setId(patientId);
            visit.setPatient(patient);
        }

        visitService.saveVisit(visit, doctorId, patientId, visitDateTime);
        redirectAttributes.addFlashAttribute("success", "Pomyślnie zarejestrowano. Dziękujemy za rejestrację!");
        return "redirect:/visit";
    }


    private String prepareVisitFormData(Model model) {
        List<PatientDto> patients = patientService.findAllPatients();
        List<DoctorDto> doctors = doctorService.findAllDoctors();
        List<LocalDateTime> availableTimes = VisitService.generateAvailableTimes();

        model.addAttribute("visit", new Visit());
        model.addAttribute("availableTimes", availableTimes);
        model.addAttribute("doctors", doctors);
        model.addAttribute("patients", patients);

        return "visit/visit";
    }

    @GetMapping(value = "/visits")
    public String listVisits(Model model, @RequestParam("page") @ModelAttribute Optional<Integer> page) {

        int currentPage = page.orElse(1);

        Page<VisitDto> visitDtoPage = visitService.findPage(currentPage);

        long totalElements = visitDtoPage.getTotalElements();
        int totalPages = visitDtoPage.getTotalPages();
        List<VisitDto> visits = visitDtoPage.getContent();

        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed().toList();
            model.addAttribute("pageNumbers", pageNumbers);
        }

        if (totalPages == 0) {
            totalPages = 1;
        }

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("visits", visits);

        return "visit/visits";
    }


    @PostMapping("/cancel")
    public String cancelVisit(@RequestParam(value = "id") Long id, Model model) {

        VisitDto visit = visitService.findVisitById(id);

        model.addAttribute("visits", visitService.findAllVisits());
        model.addAttribute("visit", visit);

        visitService.cancelVisit(visit);

        return "redirect:/visits";
    }

    @GetMapping("/cancel")
    public String showCancelVisitForm(@ModelAttribute Patient patient, Model
            model, @RequestParam(value = "id") Long id) {

        VisitDto visit = visitService.findVisitById(id);

        model.addAttribute("visit", visit);
        model.addAttribute("visits", visitService.findAllVisits());

        return "redirect:/visits";
    }
}