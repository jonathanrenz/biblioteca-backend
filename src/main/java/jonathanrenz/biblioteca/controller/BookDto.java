package jonathanrenz.biblioteca.controller;


import jonathanrenz.biblioteca.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jonathanrenz.biblioteca.repositories.bookRepository;

import java.util.List;

@RestController
@RequestMapping("books")


public class BookDto {

    @Autowired
    private bookRepository bookRepository;

    @GetMapping
    public List<Book> getAll(){
        List<Book> listBooks = bookRepository.findAll();
        return listBooks;
    }

    @PostMapping
    public ResponseEntity registerBook(@RequestBody @Validated RequestBook data) {
        Book newBook = new Book(data);
        System.out.println(data.entryDate());
        bookRepository.save(newBook);
        return ResponseEntity.ok().build();
    }


}