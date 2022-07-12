package PetServiceApp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name="pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private AnimalType type;
    @Column(name = "date_birth")
    private Date dateBirth;
    private Gender gender;
    @Column(name = "nick_name", unique = true)
    private String nickName;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;


    public Pet(AnimalType type, Date dateBirth, Gender gender, String nickName, User user) {
        this.type = type;
        this.dateBirth = dateBirth;
        this.gender = gender;
        this.nickName = nickName;
        this.user = user;
    }

    public Pet() {}
}
