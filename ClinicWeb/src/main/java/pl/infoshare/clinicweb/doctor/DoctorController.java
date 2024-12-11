package pl.infoshare.clinicweb.doctor;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.infoshare.clinicweb.patient.Address;
import pl.infoshare.clinicweb.user.Utils;
import pl.infoshare.clinicweb.user.entity.PersonDetails;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Controller
@AllArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping(value = "/doctors")
    public String listDoctors(@RequestParam(required = false) Specialization specialization, Model model,
                              @RequestParam(value = "page")
                              @ModelAttribute Optional<Integer> page,
                              @RequestParam(value = "size")
                              @ModelAttribute Optional<Integer> size) {

        int currentPage = page.orElse(1);

        Page<DoctorDto> doctorPage;

        if (specialization == null) {
            doctorPage = doctorService.findAllPage(currentPage);

        } else {
            doctorPage = doctorService.findDoctorBySpecialization(currentPage, specialization);

        }

        long totalElements = doctorPage.getTotalElements();
        int totalPages = doctorPage.getTotalPages();
        List<DoctorDto> doctors = doctorPage.getContent();

        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();
            model.addAttribute("pageNumbers", pageNumbers);
        }


        if (totalPages == 0) {
            totalPages = 1;
        }

        model.addAttribute("specialization", specialization);
        model.addAttribute("doctorsPage", doctorPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("listDoctor", doctors);


        return "doctor/doctors";
    }

    @GetMapping("/doctor")
    public String doctorForm(Model model) {

        model.addAttribute("personDetails", new PersonDetails());
        model.addAttribute("address", new Address());

        return "doctor/doctor";
    }

    @PostMapping("/doctor")
    public String doctorFormSubmission(@ModelAttribute Doctor doctor,
                                       @Valid PersonDetails doctorDetails, BindingResult detailsBinding,
                                       @Valid Address doctorAddress, BindingResult addressBinding,
                                       @RequestParam("specialization") Specialization specialization,
                                       @RequestParam("pesel") String pesel,
                                       RedirectAttributes redirectAttributes, Model model) {


        if (detailsBinding.hasErrors() || addressBinding.hasErrors() || !Utils.hasPeselCorrectDigits(pesel)) {

            model.addAttribute("peselError", "Wprowadzony numer PESEL jest niepoprawny.");
            return "doctor/doctor";

        } else {

            redirectAttributes.addFlashAttribute("success", "Utworzono nowego lekarza w bazie.");

            doctorService.setDoctorAttributes(doctor, doctorDetails, doctorAddress, specialization);
            doctorService.addDoctor(doctor);

            return "redirect:/doctor";
        }

    }

    @GetMapping("/search-doctor")
    public String searchDoctorByPesel(@ModelAttribute Doctor doctor) {

        return "doctor/search-doctor";
    }

    @PostMapping("/search-doctor")
    public String searchDoctorByPesel(@RequestParam(value = "id", required = false) long id, Model model) {

        DoctorDto doctorById = doctorService.findById(id);

        model.addAttribute("searchForId", doctorById);

        return "doctor/search-doctor";
    }

    @GetMapping("/update-doctor")
    public String fullDetailDoctor(@RequestParam(value = "id") long id, Model model) {

        model.addAttribute("doctor", doctorService.findById(id));

        return "doctor/update-doctor";
    }

    @PostMapping("/update-doctor")
    public String editDoctor(@ModelAttribute("doctor") DoctorDto doctor, Model model,
                             RedirectAttributes redirectAttributes) {

        doctorService.updateDoctor(doctor);
        model.addAttribute("doctor", doctor);
        redirectAttributes.addFlashAttribute("success", "Zaktualizowano dane lekarza.");

        return "redirect:doctors";
    }

    @GetMapping("/delete-doctor")
    public String showDeleteDoctorForm(@RequestParam("id") long id, Model model) {

        DoctorDto doctorById = doctorService.findById(id);
        model.addAttribute("doctor", doctorById);

        return "doctor/delete-doctor";
    }

    @PostMapping("/delete-doctor")
    public String deleteDoctor(@RequestParam("id") long id, RedirectAttributes redirectAttributes) {

        DoctorDto doctorById = doctorService.findById(id);

        if (doctorById != null) {

            doctorService.deleteDoctor(doctorById.getId());
            redirectAttributes.addFlashAttribute("success", "Usunięto dane lekarza.");
        }
        return "redirect:/doctors";
    }


}
