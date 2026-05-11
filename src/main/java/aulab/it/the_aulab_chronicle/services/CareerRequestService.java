package aulab.it.the_aulab_chronicle.services;

import aulab.it.the_aulab_chronicle.models.CareerRequest;
import aulab.it.the_aulab_chronicle.models.User;


public interface CareerRequestService {

    boolean isRoleAlreadyAssigned(User user, CareerRequest careerRequest);
    void save(CareerRequest careerRequest, User user);
    void careerAccept(Long requestId);
    CareerRequest find(Long id);
}
