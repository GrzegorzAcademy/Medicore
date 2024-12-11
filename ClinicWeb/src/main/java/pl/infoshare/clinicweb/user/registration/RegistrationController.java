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

            model.addAttribute("user", user);
            log.info("Validation error occured while registering user.");
            return "user/registry";
        }

        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("success", "Pomyślnie zarejestrowano użytkownika pacjenta.");

        return "redirect:/login";
    }

    @GetMapping("/register-admin")
    public String createUser(@ModelAttribute UserDto user, Model model) {


        model.addAttribute("user", user);

        log.info("New user registration form was requested.");

        return "user/registry-admin";
    }

    @PostMapping("/register-admin")
    public String userCreationFromSubmission(@Valid @ModelAttribute("user") UserDto user, BindingResult bindingResult,
                                             Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("user", user);
            log.info("Validation error occured while registering user.");
            return "user/registry-admin";
        }

        userService.saveUser(user);
        log.info("User was successfully registered with email  {}", user.getEmail());

        redirectAttributes.addFlashAttribute("success", "Pomyślnie zarejestrowano użytkownika pacjenta.");

        return "redirect:/user/registry-admin";
    }


}
