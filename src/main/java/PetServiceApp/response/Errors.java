package PetServiceApp.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Errors
{
    private ArrayList<Error> errors;
}
