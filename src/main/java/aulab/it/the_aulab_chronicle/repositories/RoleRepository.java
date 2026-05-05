package aulab.it.the_aulab_chronicle.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import aulab.it.the_aulab_chronicle.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}

