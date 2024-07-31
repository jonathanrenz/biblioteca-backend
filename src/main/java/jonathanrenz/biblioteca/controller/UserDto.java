package jonathanrenz.biblioteca.controller;

import jakarta.transaction.Transactional;
import jonathanrenz.biblioteca.domain.User;
import jonathanrenz.biblioteca.repositories.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")

public class UserDto {

    @Autowired
    private userRepository userRepository;

    @GetMapping
    public List<User> getAll() {
        List<User> listUser = userRepository.findAll();
        return listUser;
    }

    @PostMapping
    public ResponseEntity registerUser(@RequestBody RequestUser data) {

        User newUser = new User(data);
        System.out.println(data);
        userRepository.save(newUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity updateUser(@RequestBody @Validated RequestPutUser data){
        Optional<User> optionalUser = userRepository.findById(data.id());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(data.name());
            user.setEmail(data.email());
            user.setPassword(data.password());
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping

}