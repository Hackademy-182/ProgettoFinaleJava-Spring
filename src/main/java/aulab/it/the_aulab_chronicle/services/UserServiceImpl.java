package aulab.it.the_aulab_chronicle.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import jakarta.servlet.http.HttpSession;

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

    @Autowired
    private AuthenticationManager authenticationManager;

    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void saveUser(UserDTO userdto, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
        User user = new User();
        user.setUsername(userdto.getName() + " " + userdto.getSurname());
        user.setEmail(userdto.getEmail());
        user.setPassword(passwordEncoder.encode(userdto.getPassword()));

        Role role = roleRepository.findByName("ROLE_USER");
        user.setRoles(List.of(role));

        userRepository.save(user);

        authenticateUserAndSetSession(user, userdto, request);
    }

    @Override
    public User findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    private void authenticateUserAndSetSession(User user, UserDTO userdto, HttpServletRequest request) {
        try {CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
        
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userdto.getPassword());

            Authentication authentication = authenticationManager.authenticate(authToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        } catch (AuthenticationException e){
            e.printStackTrace();
        }
    }

    

}
