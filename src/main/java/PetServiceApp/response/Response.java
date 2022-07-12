package PetServiceApp.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response {

    private boolean success;
    private String message;
    private Error error;

    public Response() {}

    public void setSuccessParameters(String message) {
        success = true;
        this.message = message;
        error = null;

    }

    public void setErrorParameters(String message, Error error) {
        success = false;
        this.message = message;
        this.error = error;
    }

    public boolean getSuccess() {
        return success;
    }
}
