package PetServiceApp.security;

import PetServiceApp.models.User;
import PetServiceApp.services.UserService;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final Logger log = Logger.getLogger(SimpleUrlAuthenticationSuccessHandler.class);
    private final UserService userService;

    public CustomLoginSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("Успешная авторизация");
        User user =  (User) authentication.getPrincipal();
        userService.resetFailedAttempts(user);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
