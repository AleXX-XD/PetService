package PetServiceApp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Error {

    @JsonProperty("code")
    private int errorCode;
    @JsonProperty("message")
    private String errorMessage;

}
