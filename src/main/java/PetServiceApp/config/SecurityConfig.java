package PetServiceApp.config;

import PetServiceApp.security.CustomLoginFailureHandler;
import PetServiceApp.security.CustomLoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final CustomLoginFailureHandler loginFailureHandler;
    private final CustomLoginSuccessHandler loginSuccessHandler;

    @Autowired
    public SecurityConfig(CustomAuthenticationProvider customAuthenticationProvider,
                          CustomLoginFailureHandler loginFailureHandler,
                          CustomLoginSuccessHandler loginSuccessHandler) {
        this.loginFailureHandler = loginFailureHandler;
        this.loginSuccessHandler = loginSuccessHandler;
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                //Доступ только для незарегистрированных пользователей
                .antMatchers("/registration", "/validation").not().fullyAuthenticated()
                //Все остальные страницы требуют аутентификации
                .anyRequest().authenticated()
                .and()
                //Настройка для входа в систему
                .formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .failureHandler(loginFailureHandler)
                .successHandler(loginSuccessHandler)
                .permitAll()
                //Перенаправление на главную страницу после успешной авторизации
                .defaultSuccessUrl("/pets/").permitAll()
                .and()
                .logout().logoutUrl("/logout")
                .logoutSuccessUrl("/login").deleteCookies("JSESSIONID");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customAuthenticationProvider);
    }

}
