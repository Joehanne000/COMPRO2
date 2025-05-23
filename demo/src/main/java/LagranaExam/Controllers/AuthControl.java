package LagranaExam.Controllers;

import LagranaExam.Models.AppUser;
import LagranaExam.Services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthControl {

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new AppUser());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") @Valid AppUser formUser, BindingResult bindingResult, HttpSession session, Model model) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        // Authenticate
        AppUser foundUser = userService.findByUsername(formUser.getUsername());
        if (foundUser != null && new BCryptPasswordEncoder().matches(formUser.getPassword(), foundUser.getPassword())) {
            session.setAttribute("user", foundUser);
            return "redirect:/";
        } else {
            String error = "Invalid credentials";
            model.addAttribute("error", error);
        }

        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
