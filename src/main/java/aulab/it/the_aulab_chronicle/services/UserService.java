package aulab.it.the_aulab_chronicle.services;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import aulab.it.the_aulab_chronicle.dtos.UserDTO;
import aulab.it.the_aulab_chronicle.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    void saveUser(UserDTO userdto, RedirectAttributes redirectAttributes, HttpServletRequest request,
    HttpServletResponse response);

    User findUserByEmail(String email);
}
