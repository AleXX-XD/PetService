package PetServiceApp.security;

import PetServiceApp.models.User;
import PetServiceApp.services.UserService;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final Logger log = Logger.getLogger(SimpleUrlAuthenticationFailureHandler.class);
    private final UserService userService;

    public CustomLoginFailureHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");
        User user = userService.getUserByName(username);

        if (user != null) {
            if (user.isAccountNonLocked()) {
                if (user.getFailedAttempt() < UserService.MAX_FAILED_ATTEMPTS - 1) {
                    userService.increaseFailedAttempts(user);
                } else {
                    userService.lock(user);
                    log.warn("Аккаунт пользователя '" + user.getUsername()
                            + "' заблокирован после " + UserService.MAX_FAILED_ATTEMPTS + " неудачных попыток " +
                            "на " + UserService.LOCK_TIME_DURATION / 100000 + " мин.");
                    exception = new LockedException("Аккаунт пользователя заблокирован");
                }
            } else {
                log.warn("Аккаунт пользователя '" + user.getUsername() + "' заблокирован");
                exception = new LockedException("Аккаунт пользователя заблокирован");
            }
        }
        super.setDefaultFailureUrl("/login?error");
        super.onAuthenticationFailure(request, response, exception);
    }

}
