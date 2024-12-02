package pl.infoshare.clinicweb.user.registration;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.infoshare.clinicweb.user.service.UserService;

@Controller
@AllArgsConstructor
@Slf4j
public class RegistrationController {

    private final UserService userService;

    @GetMapping("/register")
    public String registerForm(@ModelAttribute UserDto user, Model model) {
        model.addAttribute("user", user);
        log.info("New user registration form was requested.");
        return "user/registry";
    }

    @PostMapping("/register")
    public String registerFormSubmission(@Valid @ModelAttribute("user") UserDto user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation error occurred while registering user: {}", bindingResult.getAllErrors());
            model.addAttribute("user", user);
            return "user/registry";
        }

        try {
            userService.saveUser(user);
            log.info("User registration successful for user: {}", user.getEmail());
            redirectAttributes.addFlashAttribute("success", "Pomyślnie utworzono konto użytkownika");
        } catch (Exception e) {
            log.error("Error occurred while saving user: {}", user.getEmail(), e);
            redirectAttributes.addFlashAttribute("error", "Konto użytkownika nie zostało utworzone. Proszę poprawić formularz.");
            return "user/registry";
        }

        return "redirect:/login";
    }




}
