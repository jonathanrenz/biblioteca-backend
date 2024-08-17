package jonathanrenz.biblioteca.controller;

import jonathanrenz.biblioteca.domain.User;
import jonathanrenz.biblioteca.dto.LoginRequestUser;
import jonathanrenz.biblioteca.dto.RequestUser;
import jonathanrenz.biblioteca.dto.Response;
import jonathanrenz.biblioteca.infra.security.TokenService;
import jonathanrenz.biblioteca.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")

public class AuthController {

    @Autowired
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;


    //o lombok era pra fazer isso, mas ele nao ta querendo
    public AuthController(UserRepository repository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }


    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequestUser body) {
        User user = repository.findByEmail(body.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = tokenService.generateToken(user);
            return ResponseEntity.ok(new Response(user.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RequestUser body){
        Optional<User> user = this.repository.findByEmail(body.email());

        if(user.isEmpty()) {
            User newUser = new User(body);
            newUser.setPassword(passwordEncoder.encode(body.password()));
            newUser.setEmail(body.email());
            newUser.setName(body.name());
            this.repository.save(newUser);

            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new Response(newUser.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }

}
