package PetServiceApp.repositories;

import PetServiceApp.models.AnimalType;
import PetServiceApp.models.Animals;
import org.springframework.data.repository.CrudRepository;

public interface AnimalsRepository extends CrudRepository<Animals, AnimalType> {

    Animals findByType(AnimalType type);
}
