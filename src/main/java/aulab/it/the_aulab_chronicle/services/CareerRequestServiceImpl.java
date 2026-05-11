package aulab.it.the_aulab_chronicle.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aulab.it.the_aulab_chronicle.models.CareerRequest;
import aulab.it.the_aulab_chronicle.models.Role;
import aulab.it.the_aulab_chronicle.models.User;
import aulab.it.the_aulab_chronicle.repositories.CareerRequestRepository;
import aulab.it.the_aulab_chronicle.repositories.RoleRepository;
import aulab.it.the_aulab_chronicle.repositories.UserRepository;

@Service
public class CareerRequestServiceImpl implements CareerRequestService {

    @Autowired
    private CareerRequestRepository careerRequestRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public boolean isRoleAlreadyAssigned(User user, CareerRequest careerRequest) {

        if (user == null || careerRequest.getRole() == null) {
            return false;
        }

        List<Long> requests = careerRequestRepository.findByUserId(user.getId());

        return requests.stream()
                .anyMatch(roleId -> roleId.equals(careerRequest.getRole().getId()));
    }

    @Override
    @Transactional
    public void save(CareerRequest careerRequest, User user) {

        if (user == null) {
            throw new IllegalArgumentException("Impossibile salvare: utente non trovato");
        }

        if (careerRequest.getRole() == null) {
            throw new IllegalArgumentException("Impossibile salvare: ruolo non trovato");
        }

        careerRequest.setUser(user);          
        careerRequest.setIsChecked(false);

        careerRequestRepository.save(careerRequest);

        // email all'admin
        emailService.sendSimpleEmail("admin@aulab.it", "Richiesta per il ruolo di " + careerRequest.getRole().getName(), "C'è una nuova richiesta di collaborazione da parte di " + user.getUsername());
    }

    @Override
    public void careerAccept(Long requestId) {
        
        CareerRequest request = careerRequestRepository.findById(requestId).get();

        User user = request.getUser();
        Role role = request.getRole();

        List<Role> rolesUser = user.getRoles();
        Role newRole = roleRepository.findByName(role.getName());
        rolesUser.add(newRole);

        user.setRoles(rolesUser);
        userRepository.save(user);
        request.setIsChecked(true);
        careerRequestRepository.save(request);

        emailService.sendSimpleEmail(user.getEmail(), "Ruolo abilitato", "Congratulazioni, il tuo nuovo ruolo è stato abilitato.");

        
    }

    @Override
    public CareerRequest find(Long id) {
       
        return careerRequestRepository.findById(id).get();
    }

}
