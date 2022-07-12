package PetServiceApp.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "Animals")
public class Animals {

    @Id
    @Column(name = "animal_type")
    private AnimalType type;
    @Column(name = "average_expectancy")
    private int averageExpectancy;
    private String information;

    public Animals() {
    }

}
