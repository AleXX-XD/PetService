package PetServiceApp.services;

import PetServiceApp.models.Role;
import PetServiceApp.repositories.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public Role getRoleById(int id) {
        return repository.getRoleById(id);
    }
}
