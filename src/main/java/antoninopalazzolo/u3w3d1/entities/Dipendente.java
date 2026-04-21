package antoninopalazzolo.u3w3d1.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "dipendenti")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Dipendente {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    @Setter(AccessLevel.NONE)
    private UUID id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(unique = true, nullable = false)
    private String email;
    @Column
    private String avatar;
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    public Dipendente(String username, String name, String surname, String email, String password) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.avatar = "https://ui-avatars.com/api?name=" + name + "+" + surname;
    }
}

