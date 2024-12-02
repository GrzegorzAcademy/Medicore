package pl.infoshare.clinicweb.patient;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.infoshare.clinicweb.advice.PeselFormatException;
import pl.infoshare.clinicweb.doctor.DoctorDto;
import pl.infoshare.clinicweb.doctor.DoctorService;
import pl.infoshare.clinicweb.user.Utils;
import pl.infoshare.clinicweb.user.entity.PersonDetails;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Controller
@Slf4j
public class PatientController {

    private final PatientService patientService;

    private final DoctorService doctorService;


    @GetMapping("/patient")
    public String patientForm(Model model) {
        log.info("Patient creation form retrieved.");

        model.addAttribute("personDetails", new PersonDetails());
        model.addAttribute("address", new Address());
        model.addAttribute("doctors", doctorService.findAllDoctors());

        return "patient/patient";
    }


    @PostMapping("/patient")
    public String patientFormSubmission(@ModelAttribute Patient patient,
                                        @Valid PersonDetails patientDetails, BindingResult detailsBinding,
                                        @Valid Address patientAddress, BindingResult addressBinding,
                                        @RequestParam("pesel") String pesel,
                                        Model model, RedirectAttributes redirectAttributes) {

        List<DoctorDto> doctors = doctorService.findAllDoctors();

        model.addAttribute("doctors", doctors);

        if (detailsBinding.hasErrors() || addressBinding.hasErrors()) {

            return "patient/patient";

        } else {

            redirectAttributes.addFlashAttribute("success", "Utworzono nowego pacjenta w bazie.");

            patientService.setPatientAttributes(patient, patientDetails, patientAddress);
            patientService.addPatient(patient);

            return "redirect:/patient";
        }

    }


    @GetMapping(value = "/patients")
    public String listPatients(Model model, @RequestParam(value = "pesel", required = false) String pesel,
                               @RequestParam(value = "page", required = false) Optional<Integer> page) {
        int currentPage = page.orElse(1);
        log.info("Patient list retrieved. Page: {},", currentPage);

        Page<PatientDto> patientPage = patientService.findPage(currentPage);
        int totalPages = patientPage.getTotalPages();

        model.addAttribute("pesel", pesel);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", patientPage.getTotalElements());
        model.addAttribute("listPatient", patientPage.getContent());

        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().toList();
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "patient/patients";
    }


    @GetMapping("/search")
    public String searchForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "patient/search";
    }

    @PostMapping("/search")
    public String searchPatient(@PathVariable("id") Long id, Model model, Address address) {

        PatientDto patient = patientService.findById(id);
        if (patient != null) {
            model.addAttribute("patient", patient);
            model.addAttribute("address", address);
        } else {
            model.addAttribute("error", "Patient not found");
        }
        return "patient/search";
    }

    @PostMapping("/update-patient")
    public String editPatient(@ModelAttribute("patient") PatientDto patient,
                              Model model, Address address, RedirectAttributes redirectAttributes, PersonDetails personDetails) {
        log.info("Started updating patient data: {}", patient.getId());
        try {
            patientService.updatePatient(patient, address, personDetails);
            redirectAttributes.addFlashAttribute("success", "Zaktualizowano dane pacjenta.");
            log.info("Patient data has been updated: {}", patient.getId());
        } catch (Exception e) {
            log.error("An error occurred while updating patient data: {}", patient.getId(), e);
            redirectAttributes.addFlashAttribute("error", "Wystąpił błąd podczas aktualizacji danych pacjenta.");
        }

        return "redirect:/patients";
    }


    @GetMapping("/update-patient")
    public String fullDetailPatient(@RequestParam(value = "id", required = false)
                                    @ModelAttribute Long id,
                                    Model model) {

        model.addAttribute("patient", patientService.findById(id));


        return "patient/update-patient";
    }

    @GetMapping("/search-patient")
    public String searchPatientByPesel(Model model, @RequestParam(value = "pesel", required = false) String pesel) {
        log.info("Started searching for patient by PESEL number: {}", pesel);

        if (pesel == null || !Utils.hasPeselCorrectDigits(pesel)) {
            log.warn("Invalid PESEL number format: {}", pesel);
            throw new PeselFormatException(pesel);
        }
        PatientDto patientByPesel = patientService.findByPesel(pesel);
        if (patientByPesel != null) {
            model.addAttribute("patientByPesel", patientByPesel);
        } else {
            log.info("No patient found with PESEL number: {}", pesel);
            model.addAttribute("error", "Nie znaleziono pacjenta.");
        }

        return "patient/search-patient";
    }


    @PostMapping("/delete-patient")
    public String deletePatient(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        log.info("Rozpoczęto usuwanie pacjenta o id: {}", id);

        try {
            PatientDto patientById = patientService.findById(id);
            if (patientById != null) {
                patientService.deletePatient(id);
                redirectAttributes.addFlashAttribute("success", "Pacjent został pomyślnie usunięty.");
                log.info("Patient with ID has been deleted: {}", id);
            } else {
                log.warn("No patient found with ID: {}", id);
                redirectAttributes.addFlashAttribute("error", "Nie znaleziono pacjenta o podanym id.");
            }
        } catch (Exception e) {
            log.error("An error occurred while deleting patient with ID: {}", id, e);
            redirectAttributes.addFlashAttribute("error", "Wystąpił błąd podczas usuwania pacjenta.");
        }

        return "redirect:/patients";
    }


    @GetMapping("/delete-patient")
    public String showDeletePatientForm(@RequestParam("id") Long id, Model model) {

        PatientDto patientById = patientService.findById(id);

        model.addAttribute("patient", patientById);

        return "patient/delete-patient";
    }


}
