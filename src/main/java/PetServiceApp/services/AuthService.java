package PetServiceApp.services;

import PetServiceApp.models.LoginForm;
import PetServiceApp.models.User;
import PetServiceApp.response.Response;
import PetServiceApp.utils.GetErrorsUtil;
import org.apache.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthService {

    private final Logger log = Logger.getLogger(AuthService.class);
    private final UserService userService;
    private final ValidationService validationService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder,
                       ValidationService validationService) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.validationService = validationService;
    }

    public Response registration(LoginForm loginForm) throws IOException {
        String loginErrorMessage = "Ошибка при регистрации";
        Response response = new Response();
        if (loginForm.getUsername() == null || loginForm.getPassword() == null) {
            log.warn("RegistrationService -> Не верные параметры запроса!");
            response.setErrorParameters(loginErrorMessage, GetErrorsUtil.getError(101));
        } else {
            Response validationResponse = validationService.authValidation(loginForm.getUsername());
            if (!validationResponse.getSuccess()) {
                response = validationResponse;
            } else if (!loginForm.getPassword().matches("[\\w\\p{Punct}]{5,}")) {
                log.warn("RegistrationService -> Пароль не соответсвует требованиям");
                response.setErrorParameters(loginErrorMessage, GetErrorsUtil.getError(202));
            } else {
                User user = new User(loginForm.getUsername(), loginForm.getPassword());
                user.resetFailedAttempt();
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                userService.save(user);
                log.info("RegistrationService -> Новый пользователь '" + loginForm.getUsername() + "' зарегистрирован");
                response.setSuccessParameters("Новый пользователь '" + loginForm.getUsername() + "' зарегистрирован");
            }
        }
        return response;
    }
}
