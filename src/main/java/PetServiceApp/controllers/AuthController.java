package PetServiceApp.controllers;

import PetServiceApp.models.LoginForm;
import PetServiceApp.models.User;
import PetServiceApp.response.Response;
import PetServiceApp.services.AuthService;
import PetServiceApp.services.ValidationService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Controller
public class AuthController {

    private final AuthService authService;
    private final ValidationService validationService;
    private final Logger log = Logger.getLogger(AuthController.class);

    public AuthController(AuthService authService, ValidationService validationService) {
        this.authService = authService;
        this.validationService = validationService;
    }

    @GetMapping("/")
    public ResponseEntity mainPage() {
        log.debug("GET /");
        Response response = new Response();
        response.setSuccessParameters("Загружен список питомцев пользователя -> /pets");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/registration")
    public ResponseEntity registration() {
        log.debug("GET /registration");
        Response response = new Response();
        response.setSuccessParameters("Осуществлен переход на страницу регистрации -> /registration");
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/registration")
    public ResponseEntity addUser(LoginForm loginForm) throws IOException {
        log.debug("POST /registration. Получено : " + loginForm);
        Response response = authService.registration(loginForm);
        if(response.getSuccess()) {
            return ResponseEntity.ok().body(response);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/login")
    public ResponseEntity login() {
        log.debug("GET /login");
        Response response = new Response();
        response.setSuccessParameters("Осуществлен переход на страницу -> /login ");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        log.debug("GET /logout");
        HttpSession session = request.getSession();
        SecurityContextHolder.clearContext();
        if (session != null) {
            session.invalidate();
        }
        for (Cookie cookie : request.getCookies()) {
            cookie.setMaxAge(0);
        }
        Response response = new Response();
        response.setSuccessParameters("Осуществлен выход пользователя -> /logout");
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/validation")
    public ResponseEntity checkValidation(User user) throws IOException {
        log.debug("POST /validation. Получено : " + user.getUsername());
        Response response = validationService.authValidation(user.getUsername());
        if(response.getSuccess()) {
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
