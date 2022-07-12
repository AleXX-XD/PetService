package PetServiceApp.repositories;

import PetServiceApp.models.AnimalType;
import PetServiceApp.models.Pet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends CrudRepository<Pet, Integer> {

    List<Pet> findAllByUserId(int userId);
    Pet findPetByNickNameAndUserId(String nickName, int userId);
    List<Pet> findAllByTypeAndUserId(AnimalType type, int userId);
    Pet findPetByNickName(String nickName);

}
