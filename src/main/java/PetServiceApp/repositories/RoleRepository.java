package PetServiceApp.repositories;

import PetServiceApp.models.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {

    Role getRoleById(int id);
}
