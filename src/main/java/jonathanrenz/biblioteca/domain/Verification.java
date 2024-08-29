package jonathanrenz.biblioteca.domain;


import jakarta.persistence.*;
import lombok.*;


@Entity(name = "codEmail")
@Table(name = "codEmail")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Verification{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String cod;

}
