package PetServiceApp.services;

import PetServiceApp.models.*;
import PetServiceApp.response.Response;
import PetServiceApp.utils.GetErrorsUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Service
public class ValidationService {

    private final Logger log = Logger.getLogger(ValidationService.class);
    private final UserService userService;
    private final PetService petService;

    public ValidationService(UserService userService, PetService petService) {
        this.userService = userService;
        this.petService = petService;
    }

    public Response authValidation(String name) throws IOException {
        Response response = new Response();
        String validationErrorMessage = "Ошибка валидации";
        if (name == null) {
            log.warn("ValidationService -> Не задан параметр запроса");
            response.setErrorParameters(validationErrorMessage, GetErrorsUtil.getError(101));
        } else if (!name.matches("[a-zA-Z0-9_]{3,}")) {
            log.warn("ValidationService -> Логин не соответсвует требованиям");
            response.setErrorParameters(validationErrorMessage, GetErrorsUtil.getError(201));
        } else if (userService.getUserByName(name) == null) {
            log.info("ValidationService -> '" + name + "' - доступное имя пользователя");
            response.setSuccessParameters("Доступное имя пользователя");
        } else {
            log.warn("ValidationService -> Пользователь с указанным именем уже существует");
            response.setErrorParameters(validationErrorMessage, GetErrorsUtil.getError(203));
        }
        return response;
    }

    public Response checkPetValidation(PetDetails petDetails) throws IOException {
        Response response = new Response();
        response.setSuccessParameters("Валидация пройдена. Параметры корректны");
        if (!checkNameValidation(petDetails.getNickName())) {
            response.setErrorParameters("Ошибка параметров запроса", GetErrorsUtil.getError(206));
        } else if (petService.getPet(petDetails.getNickName().toLowerCase(Locale.ROOT)) != null) {
            response.setErrorParameters("Ошибка параметров запроса", GetErrorsUtil.getError(203));
        } else if (!checkDateValidation(petDetails.getDateBirth())){
            response.setErrorParameters("Ошибка параметров запроса", GetErrorsUtil.getError(205));
        }
        return response;
    }

    public boolean checkNameValidation(String name) {
        return name.matches("[a-zA-Zа-яА-ЯёЁ0-9-_\\s]+");
    }

    public boolean checkDateValidation(String checkDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            Date date = formatter.parse(checkDate);
            return !date.after(new Date());
        } catch (ParseException ex) {
            return false;
        }
    }
}
