package pl.infoshare.clinicweb.patientCard;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class PatientCardController {


    private final PatientCardService patientCardService;
    private final VisitService visitService;
    private final PatientCardMapper patientCardMapper;

    @GetMapping("/patient-card")
    public String createPatientCard(@RequestParam(value = "id", required = false)
                                    Long id, Model model) {

        VisitDto visit = visitService.findVisitById(id);
        PatientCardDTO patientCardDTO = PatientCardService.getPatientCardDTO(visit);

        model.addAttribute("visit", visit);
        model.addAttribute("patientCard", patientCardDTO);

        return "patient/patient-card";
    }


    @GetMapping("/detail-patient-appointments")
    public String getDetailPatientAppointments(@RequestParam(value = "id", required = false) Long id, Model model) {


        List<PatientCard> patientAppointments = patientCardService.findAllPatientCardByPatientId(id);


        PatientCardDTO matchingPatientCard = patientCardService.findById(id);


        model.addAttribute("matchingPatientCard", matchingPatientCard);


        model.addAttribute("patientAppointments", patientAppointments);


        return "patient/detail-patient-appointments";
    }


    @GetMapping("/patient-appointments")
    public String getPatientAppointments(@RequestParam(value = "id", required = false) Long id, Model model) {

        List<PatientCard> patientAppointments = patientCardService.findAllPatientCardByPatientId(id);
        model.addAttribute("patientAppointments", patientAppointments);
        return "patient/patient-appointments";
    }

    @PostMapping("/patient-card")
    public String savePatientCard(
            @Valid @ModelAttribute("patientCard") PatientCardDTO patientCardDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Błąd zapisu karty pacjenta");
            return "patient-card";
        }

        PatientCard patientCard = patientCardMapper.toEntity(patientCardDTO);

        patientCardService.patientCardSave(patientCard);
        redirectAttributes.addFlashAttribute("successMessage", "Karta pacjenta została pomyślnie zapisana!");
        return "patient/patient-appointments";
    }


}

