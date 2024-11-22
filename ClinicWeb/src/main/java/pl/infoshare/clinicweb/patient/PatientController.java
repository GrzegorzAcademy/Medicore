package pl.infoshare.clinicweb.patient;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Controller
public class PatientController {

    private final PatientService patientService;

    private final DoctorService doctorService;


    @GetMapping("/patient")
    public String patientForm(Model model) {

        List<DoctorDto> doctors = doctorService.findAllDoctors();

        model.addAttribute("personDetails", new PersonDetails());
        model.addAttribute("address", new Address());
        model.addAttribute("doctors", doctors);

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
                               @RequestParam(value = "page") @ModelAttribute Optional<Integer> page) {


        int currentPage = page.orElse(1);

        Page<PatientDto> patientPage = patientService.findPage(currentPage);

        long totalElements = patientPage.getTotalElements();
        int totalPages = patientPage.getTotalPages();
        List<PatientDto> patients = patientPage.getContent();

        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        if (totalPages == 0) {
            totalPages = 1;
        }

        model.addAttribute("pesel", pesel);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("listPatient", patients);

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
                              Model model, Address address, RedirectAttributes redirectAttributes) {

        model.addAttribute("patient", patient);
        model.addAttribute("address", address);

        patientService.updatePatient(patient, address);
        redirectAttributes.addFlashAttribute("success", "Zaktualizowano dane pacjenta.");
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


        if (!Utils.hasPeselCorrectDigits(pesel) || pesel == null) {

            throw new PeselFormatException(pesel);

        } else {

            PatientDto patientByPesel = patientService.findByPesel(pesel);
            model.addAttribute("patientByPesel", patientByPesel);
        }

        return "patient/search-patient";

    }

    @PostMapping("/delete-patient")
    public String deletePatient(@RequestParam("id") Long id) {

        PatientDto patientById = patientService.findById(id);
        if (patientById != null) {
            patientService.deletePatient(id);
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
