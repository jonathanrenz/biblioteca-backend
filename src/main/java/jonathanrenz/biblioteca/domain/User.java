package jonathanrenz.biblioteca.domain;


import jakarta.persistence.*;
import jonathanrenz.biblioteca.dto.RequestUser;
import lombok.*;


@Entity (name = "users")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")

public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;


    public User(RequestUser requestUser){
        this.name = requestUser.name();
        this.email = requestUser.email();
        this.password = requestUser.password();
    }


}