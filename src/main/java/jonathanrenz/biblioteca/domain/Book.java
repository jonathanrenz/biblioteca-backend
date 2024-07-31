package jonathanrenz.biblioteca.domain;


import jakarta.persistence.*;
import jonathanrenz.biblioteca.controller.RequestBook;
import lombok.*;


@Entity(name = "book")
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")

public class Book {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private String entryDate;


    public Book(RequestBook requestBook) {
        
    }


    public void setName(String name) {
    }

    public void setType(String type) {
    }

    public void setEntryDate(String s) {
    }
}