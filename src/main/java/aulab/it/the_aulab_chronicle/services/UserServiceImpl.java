package aulab.it.the_aulab_chronicle.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import aulab.it.the_aulab_chronicle.dtos.UserDTO;
import aulab.it.the_aulab_chronicle.models.Role;
import aulab.it.the_aulab_chronicle.models.User;
import aulab.it.the_aulab_chronicle.repositories.RoleRepository;
import aulab.it.the_aulab_chronicle.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Override
    public void saveUser(UserDTO userdto, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
        User user = new User();
        user.setUsername(userdto.getName() + " " + userdto.getSurname());
        user.setEmail(userdto.getEmail());
        user.setPassword(passwordEncoder.encode(userdto.getPassword()));

        Role role = roleRepository.findByName("ROLE_USER");
        user.setRoles(List.of(role));

        userRepository.save(user);

        authenticateUserAndSetSession(user, request);
    }

    @Override
    public User findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    private void authenticateUserAndSetSession(User user, HttpServletRequest request) {

    UserDetails userDetails =
            customUserDetailsService.loadUserByUsername(user.getEmail());

    Authentication authentication =
            new UsernamePasswordAuthenticationToken(userDetails,null,
                                                    userDetails.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(authentication);

    request.getSession(true)
            .setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
}

    

}
