package PetServiceApp.services;

import PetServiceApp.models.AnimalType;
import PetServiceApp.models.Pet;
import PetServiceApp.models.PetDetails;
import PetServiceApp.models.User;
import PetServiceApp.repositories.PetRepository;
import PetServiceApp.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public PetService(PetRepository petRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    public void save(PetDetails petDetails, String username) throws ParseException {
        User user = userRepository.findByUsername(username);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date date = formatter.parse(petDetails.getDateBirth());
        Pet pet = new Pet(petDetails.getType(), date, petDetails.getGender(), petDetails.getNickName().toLowerCase(Locale.ROOT), user);
        petRepository.save(pet);
    }

    public long getCount() {
        return petRepository.count();
    }

    public List<Pet> getAllAnimals (String username) {
        User user = userRepository.findByUsername(username);
        return petRepository.findAllByUserId(user.getId());
    }

    public Pet getPet(String nickName, String username) {
        User user = userRepository.findByUsername(username);
        return petRepository.findPetByNickNameAndUserId(nickName, user.getId());
    }

    public Pet getPet(String nickName) {
        return petRepository.findPetByNickName(nickName);
    }

    public List<Pet> getPets(AnimalType type, String username) {
        User user = userRepository.findByUsername(username);
        return  petRepository.findAllByTypeAndUserId(type, user.getId());
    }

    public void deletePet(String nickName, String username) {
        User user = userRepository.findByUsername(username);
        Pet pet = petRepository.findPetByNickNameAndUserId(nickName, user.getId());
        petRepository.delete(pet);
    }

    public void putPet(Pet pet, PetDetails petDetails) throws ParseException {
        if (petDetails.getDateBirth() != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            Date date = formatter.parse(petDetails.getDateBirth());
            pet.setDateBirth(date);
        }
        if(petDetails.getNickName() != null) {
            pet.setNickName(petDetails.getNickName().toLowerCase(Locale.ROOT));
        }
        if(petDetails.getGender() != null) {
            pet.setGender(petDetails.getGender());
        }
        if(petDetails.getType() != null) {
            pet.setType(petDetails.getType());
        }
        petRepository.save(pet);
    }
}
