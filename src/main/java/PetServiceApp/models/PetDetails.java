package PetServiceApp.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetDetails {

    private AnimalType type;
    private String dateBirth;
    private Gender gender;
    private String nickName;

}
