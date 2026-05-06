package aulab.it.the_aulab_chronicle.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import aulab.it.the_aulab_chronicle.dtos.UserDTO;
import aulab.it.the_aulab_chronicle.models.User;
import aulab.it.the_aulab_chronicle.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // Home

    @GetMapping("/")
    public String home(){
        return "home";
    }

    // Register

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new UserDTO());
        return "auth/register";
    }

    // Login

    @GetMapping("/login")
    public String login(){
        return "auth/login";
    }

    // Salvataggio registrazione

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDTO userDTO, 
                                BindingResult result, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response){
        
        User existingUser = userService.findUserByEmail(userDTO.getEmail());
        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
           result.rejectValue("email", null, "C'è già un account registrato con questa email"); 
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDTO);
            return "auth/register";
        }

        userService.saveUser(userDTO, redirectAttributes, request, response);
        redirectAttributes.addFlashAttribute("successMessage", "Registrazione avvenuta con successo");
        return "redirect:/";
    } 

                                
}
