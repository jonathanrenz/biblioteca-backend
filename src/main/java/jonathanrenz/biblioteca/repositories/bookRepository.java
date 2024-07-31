package jonathanrenz.biblioteca.repositories;

import jonathanrenz.biblioteca.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface bookRepository extends JpaRepository<Book, Long> {


}