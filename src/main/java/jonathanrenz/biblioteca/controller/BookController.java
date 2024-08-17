package jonathanrenz.biblioteca.controller;


import jakarta.transaction.Transactional;
import jonathanrenz.biblioteca.domain.Book;
import jonathanrenz.biblioteca.dto.RequestPutBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jonathanrenz.biblioteca.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("books")


public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public List<Book> getAll(){
        List<Book> listBooks = bookRepository.findAll();
        return listBooks;
    }

    @PutMapping
    @Transactional
    public ResponseEntity updateBook(@RequestBody @Validated RequestPutBook data){
        Optional<Book> optionalBook = bookRepository.findById(data.id());
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setName(data.name());
            book.setType(data.type());
            book.setEntryDate(data.entryDate());
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    bookRepository.delete(book);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}