package pl.infoshare.clinicweb.user.login;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.infoshare.clinicweb.user.entity.User;

@Controller
@AllArgsConstructor
@Slf4j
public class LoginController {

    @GetMapping("/")
    public String getIndex() {

        return "home/index";
    }

    @GetMapping("/login")
    String login(Model model) {

        var user = getPrincipal();

        if (user != null) {


            log.info("User with email: {} was successfully logged in.", user.getEmail());
            return "redirect:/index";

        }

        return "user/login";
    }

    @GetMapping("/logout")
    String logout() {

        return "home/index";

    }

    private User getPrincipal() {
        User user = null;
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User) {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return user;
    }


}
