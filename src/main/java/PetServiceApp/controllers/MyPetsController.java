package PetServiceApp.controllers;

import PetServiceApp.models.AnimalType;
import PetServiceApp.models.Pet;
import PetServiceApp.models.PetDetails;
import PetServiceApp.response.Response;
import PetServiceApp.services.PetService;
import PetServiceApp.services.ValidationService;
import PetServiceApp.utils.GetErrorsUtil;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping(value = "/pets/")
public class MyPetsController {

    private final PetService petService;
    private final ValidationService validationService;
    private final Logger log = Logger.getLogger(MyPetsController.class);
    private final String petError = "Ошибка при работе с таблицей Pets -> /pets/";

    public MyPetsController(PetService petService, ValidationService validationService) {
        this.petService = petService;
        this.validationService = validationService;
    }

    @GetMapping(value = "/")
    public ResponseEntity getAllPets(HttpServletRequest servletRequest) {
        log.debug("GET /pets/");
        String username = servletRequest.getRemoteUser();
        Response response = new Response();
        if (petService.getCount() == 0) {
            log.info("Список животных пользователя '" + username + "' пуст");
            response.setSuccessParameters("Список животных пользователя '" + username + "' - пуст");
            return ResponseEntity.ok().body(response);
        } else {
            List<Pet> petsList = petService.getAllAnimals(username);
            log.info("Выведен список всех животных пользователя");
            return ResponseEntity.ok().body(petsList);
        }
    }

    @PostMapping(value = "/")
    public ResponseEntity addPet(PetDetails petDetails, HttpServletRequest servletRequest) throws ParseException, IOException {
        log.debug("POST /pets/nickname/");
        Response response = validationService.checkPetValidation(petDetails);
        String username = servletRequest.getRemoteUser();
        if (response.getSuccess()) {
            petService.save(petDetails, username);
            response.setSuccessParameters(petDetails.getNickName() + "' добавлен в список питомцев пользователя '" + username + "'");
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping(value = "/type/{type}")
    public ResponseEntity getPet(@PathVariable AnimalType type, HttpServletRequest servletRequest) throws IOException {
        log.debug("GET /pets/type/" + type);
        Response response = new Response();
        String username = servletRequest.getRemoteUser();
        List<Pet> pets = petService.getPets(type, username);
        if (pets.size() == 0) {
            log.warn("Питомцев с типом '" + type + "' у пользователя '" + username + "' нет");
            response.setErrorParameters(petError, GetErrorsUtil.getError(301));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            log.info("Выведены все питомцы типа '" + type + "' для пользователя '" + username + "'");
            return ResponseEntity.ok().body(pets);
        }
    }

    @GetMapping(value = "nickname/{nickName}")
    public ResponseEntity getPet(@PathVariable String nickName, HttpServletRequest servletRequest) throws IOException {
        log.debug("GET /pets/nickname/" + nickName);
        nickName = nickName.toLowerCase(Locale.ROOT);
        Response response = new Response();
        String username = servletRequest.getRemoteUser();
        Pet pet = petService.getPet(nickName, username);
        if (pet == null) {
            log.warn("Питомца с кличкой '" + nickName + "' у пользователя '" + username + "' нет");
            response.setErrorParameters(petError, GetErrorsUtil.getError(301));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            log.info("Выведены данные питомца '" + nickName + "' для пользователя '" + username + "'");
            return ResponseEntity.ok().body(pet);
        }
    }

    @DeleteMapping(value = "nickname/{nickName}")
    public ResponseEntity deletePet(@PathVariable String nickName, HttpServletRequest servletRequest) throws IOException {
        log.debug("DELETE /pets/nickname/" + nickName);
        nickName = nickName.toLowerCase(Locale.ROOT);
        Response response = new Response();
        String username = servletRequest.getRemoteUser();
        Pet pet = petService.getPet(nickName, username);
        if (pet == null) {
            log.warn("Питомца с кличкой '" + nickName + "' у пользователя '" + username + "' нет");
            response.setErrorParameters(petError, GetErrorsUtil.getError(301));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            petService.deletePet(nickName, username);
            response.setSuccessParameters("Питомец '" + nickName + "' удален");
            log.info("Пользователь '" + username + "' удалил питомца '" + nickName + "'");
            return ResponseEntity.ok().body(response);
        }
    }

    @PutMapping(value = "nickname/{nickName}")
    public ResponseEntity putPet(@PathVariable String nickName, PetDetails petDetails, HttpServletRequest servletRequest) throws ParseException, IOException {
        log.debug("PUT /pets/nickname/" + nickName);
        nickName = nickName.toLowerCase(Locale.ROOT);
        Response response = new Response();
        String username = servletRequest.getRemoteUser();
        Pet pet = petService.getPet(nickName, username);
        if (pet == null) {
            log.warn("Питомца с кличкой '" + nickName + "' у пользователя '" + username + "' нет");
            response.setErrorParameters(petError, GetErrorsUtil.getError(301));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            response.setSuccess(true);
            PetDetails newDetails = new PetDetails();
            boolean result;
            if (petDetails.getNickName() != null) {
                result = validationService.checkNameValidation(petDetails.getNickName());
                if (!result) {
                    response.setErrorParameters("Ошибка параметров запроса", GetErrorsUtil.getError(206));
                } else {
                    newDetails.setNickName(petDetails.getNickName().toLowerCase(Locale.ROOT));
                }
            }
            if (petDetails.getDateBirth() != null) {
                result = validationService.checkDateValidation(petDetails.getDateBirth());
                if (!result) {
                    response.setErrorParameters("Ошибка параметров запроса", GetErrorsUtil.getError(205));
                } else {
                    newDetails.setDateBirth(petDetails.getDateBirth());
                }
            }
            if (petDetails.getType() != null) {
                newDetails.setType(petDetails.getType());
            }
            if (petDetails.getGender() != null) {
                newDetails.setGender(petDetails.getGender());
            }
            if(response.getSuccess()) {
                petService.putPet(pet, newDetails);
                response.setSuccessParameters("Данные питомца '" + nickName + "' обновлены");
                log.info("Пользователь '" + username + "' обновил данные питомца '" + nickName + "'");
                return ResponseEntity.ok().body(response);
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }
    }
}
