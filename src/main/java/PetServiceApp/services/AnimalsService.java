package PetServiceApp.services;

import PetServiceApp.models.AnimalType;
import PetServiceApp.models.Animals;
import PetServiceApp.repositories.AnimalsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnimalsService {

    private final AnimalsRepository repository;

    public AnimalsService(AnimalsRepository repository) {
        this.repository = repository;
    }

    public List<Animals> getAllAnimals() {
        Iterable<Animals> animals = repository.findAll();
        List<Animals> animalsList = new ArrayList<>();
        animals.forEach(animalsList::add);
        return animalsList;
    }

    public long getCount() {
        return repository.count();
    }

    public Animals getAnimalByType(AnimalType type) {
        return repository.findByType(type);
    }

}
