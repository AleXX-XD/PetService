package PetServiceApp.controllers;

import PetServiceApp.models.AnimalType;
import PetServiceApp.models.Animals;
import PetServiceApp.response.Response;
import PetServiceApp.services.AnimalsService;
import PetServiceApp.utils.GetErrorsUtil;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/animals/")
public class AnimalsController {

    private final AnimalsService animalsService;
    private final Logger log = Logger.getLogger(AnimalsController.class);

    public AnimalsController(AnimalsService animalsService) {
        this.animalsService = animalsService;
    }

    @GetMapping(value = "/")
    public ResponseEntity getAllAnimals() throws IOException {
        log.debug("GET /animals/");
        Response response = new Response();
        if (animalsService.getCount() == 0) {
            log.warn("Список животных пуст");
            String animalsError = "Ошибка при работе с таблицей Animals -> /animals/";
            response.setErrorParameters(animalsError, GetErrorsUtil.getError(301));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        List<Animals> animalsList = animalsService.getAllAnimals();
        response.setSuccessParameters("Выведен список всех животных -> /animals/");
        log.info("Выведен список всех животных");
        return ResponseEntity.ok().body(animalsList);
    }

    @GetMapping(value = "{type}")
    public ResponseEntity getAnimal(@PathVariable AnimalType type) {
        log.debug("GET /animals/{type}");
        Animals animal = animalsService.getAnimalByType(type);
        return ResponseEntity.ok().body(animal);
    }
}
