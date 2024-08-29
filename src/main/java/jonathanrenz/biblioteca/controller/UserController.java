package jonathanrenz.biblioteca.controller;

import jakarta.transaction.Transactional;
import jonathanrenz.biblioteca.domain.User;
import jonathanrenz.biblioteca.dto.RequestPutUser;
import jonathanrenz.biblioteca.dto.RequestUser;
import jonathanrenz.biblioteca.dto.Response;
import jonathanrenz.biblioteca.infra.security.TokenService;
import jonathanrenz.biblioteca.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")

public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @GetMapping
    public List<User> getAll() {
        List<User> listUser = userRepository.findAll();
        return listUser;
    }

    @PostMapping
    public ResponseEntity registerUser(@RequestBody RequestUser data) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(data.password());
        User newUser = new User(data);
        newUser.setPassword(encodedPassword);
        userRepository.save(newUser);
        return ResponseEntity.ok().build();
    }


    @PutMapping
    @Transactional
    public ResponseEntity updateUser(@RequestBody RequestPutUser data) {
        Optional<User> optionalUser = userRepository.findById(data.id());


        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            user.setName(data.name());
            user.setEmail(data.email());
            user.setPassword(passwordEncoder.encode(data.password()));
            userRepository.save(user);

            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new Response(user.getName(), user.getEmail(), token));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}