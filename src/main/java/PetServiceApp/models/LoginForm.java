package PetServiceApp.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {

    private String username;
    private String password;

    @Override
    public String toString() {
        return "{username='" + username + "' , password='" + password + "'}";
    }
}
