package PetServiceApp.config;

import PetServiceApp.models.User;
import PetServiceApp.repositories.UserRepository;
import PetServiceApp.services.UserService;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository repository;
    private final UserService service;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Logger log = Logger.getLogger(CustomAuthenticationProvider.class);

    public CustomAuthenticationProvider(UserRepository repository, UserService service, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.repository = repository;
        this.service = service;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();
        User myUser = repository.findByUsername(userName);
        if (myUser == null) {
            log.warn("Попытка входа неизвестного пользователя : " + userName);
            throw new BadCredentialsException("Unknown user " + userName);
        }

        if (!bCryptPasswordEncoder.matches(password, myUser.getPassword())) {
            log.warn("Попытка входа пользователя '" + userName + "' с неправильным паролем");
            throw new BadCredentialsException("Bad password");
        }
        if (!myUser.isAccountNonLocked()) {
            if (service.unlockWhenTimeExpired(myUser)) {
                log.warn("Время блокировки истекло. Аккаунт пользователя '" + myUser.getUsername() + "' разблокирован.");
            } else {
                throw new LockedException("User is locked");
            }
        }
        service.resetFailedAttempts(myUser);
        UserDetails principal = User.builder()
                .setUsername(myUser.getUsername())
                .setPassword(myUser.getPassword())
                .setRoles(myUser.getRoles())
                .setAccountNonLocked(myUser.isAccountNonLocked())
                .build();
        log.info("Осуществлен вход пользователя : " + userName);
        return new UsernamePasswordAuthenticationToken(
                principal, password, principal.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
