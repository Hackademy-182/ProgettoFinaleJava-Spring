package aulab.it.the_aulab_chronicle.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import aulab.it.the_aulab_chronicle.models.CareerRequest;
import aulab.it.the_aulab_chronicle.models.Role;
import aulab.it.the_aulab_chronicle.models.User;
import aulab.it.the_aulab_chronicle.repositories.RoleRepository;
import aulab.it.the_aulab_chronicle.repositories.UserRepository;
import aulab.it.the_aulab_chronicle.services.CareerRequestService;

@Controller
@RequestMapping("/operations")
public class OperationController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CareerRequestService careerRequestService;

    

    // Rotta creazione richiesta di collaborazione

    @GetMapping("/career/request")
    public String careerRequestCreate(Model viewModel){
        viewModel.addAttribute("title", "Creazione richiesta di collaborazione");
        viewModel.addAttribute("careerRequest", new CareerRequest());

        List<Role> roles = roleRepository.findAll();
        roles.removeIf(e->e.getName().equals("ROLE_USER"));
        viewModel.addAttribute("roles", roles);

        return "/career/requestForm";
    }

    // Rotta salvataggio richiesta

    @PostMapping("/career/request/save")
    public String careerRequestStore(@ModelAttribute("careerRequest") CareerRequest careerRequest,
                                    Principal principal,
                                    RedirectAttributes redirectAttributes){

       

        User user = userRepository.findByUsername(principal.getName());

        if (user == null) {
            throw new IllegalStateException("USER NULL - PRINCIPAL: " + principal.getName());
        }

        careerRequestService.save(careerRequest, user);

        redirectAttributes.addFlashAttribute("successMessage", 
                                    "Richiesta di collaborazione inviata con successo");

        return "redirect:/";
    }

    // Rotta dettaglio richiesta

    @GetMapping("/career/request/detail/{id}")
    public String careerRequestDetail(@PathVariable("id") Long id, Model viewModel){
        viewModel.addAttribute("title", "Dettaglio richiesta");
        viewModel.addAttribute("request", careerRequestService.find(id));

        return "career/requestDetail";
    }

    // Rotta accettazione richiesta

    @PostMapping("/career/request/accept/{requestId}")
    public String careerRequestAccept(@PathVariable Long requestId, RedirectAttributes redirectAttributes){
        careerRequestService.careerAccept(requestId);
        redirectAttributes.addFlashAttribute("successMessage", "Il ruolo richiesto dall'utente è stato abilitato");

        return "redirect:/admin/dashboard";
    }

}
