package jonathanrenz.biblioteca.controller;

import jakarta.transaction.Transactional;
import jdk.swing.interop.SwingInterOpUtils;
import jonathanrenz.biblioteca.domain.UpdatePassword;
import jonathanrenz.biblioteca.domain.User;
import jonathanrenz.biblioteca.domain.Verification;
import jonathanrenz.biblioteca.dto.*;
import jonathanrenz.biblioteca.infra.security.TokenService;
import jonathanrenz.biblioteca.infra.services.EmailService;
import jonathanrenz.biblioteca.infra.services.UserService;
import jonathanrenz.biblioteca.repositories.UserRepository;

import jonathanrenz.biblioteca.repositories.VerificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Optional;


@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    @Autowired
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final VerificationRepository verificationRepository;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequestUser body) {
        User user = repository.findByEmail(body.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = tokenService.generateToken(user);
            return ResponseEntity.ok(new Response(user.getName(), user.getEmail(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RequestUser body) {
        Optional<User> user = this.repository.findByEmail(body.email());

        if (user.isEmpty()) {
            User newUser = new User(body);
            newUser.setPassword(passwordEncoder.encode(body.password()));
            newUser.setEmail(body.email());
            newUser.setName(body.name());
            this.repository.save(newUser);

            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new Response(newUser.getName(), newUser.getEmail(), token));
        }
        return ResponseEntity.badRequest().build();
    }


    @PutMapping("/recovery")
    public ResponseEntity recoveryPassword(@RequestBody RequestPutUser body) {
        Optional<User> optionalUser = this.repository.findByEmail(body.email());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(body.name());
            user.setEmail(body.email());
            user.setPassword(body.password());
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/existsByEmail")
    @Transactional
    public ResponseEntity<Boolean> checkIfEmailExists(@RequestParam String email) {
        boolean exists = repository.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @Autowired
    private EmailService emailService;

    @PostMapping("/sendingEmail")
    @Transactional
    public ResponseEntity<String> sendingEmail(@RequestParam String email) {

        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("E-mail não pode ser vazio.");
        }
        Optional<User> optionalUser = this.repository.findByEmail(email);
        if (optionalUser.isPresent()) {

            String verificationCode = generateVerificationCode();

            Verification verification = new Verification();
            verification.setEmail(email);
            verification.setCod(verificationCode);
            verificationRepository.save(verification);

            emailService.sendEmail(email, "Email para recuperacao de senha.", "Aqui esta seu código para recuperacao:" + verificationCode);


            return ResponseEntity.ok("E-mail de recuperação enviado com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("E-mail não encontrado.");
        }
    }

    private static final SecureRandom random = new SecureRandom();

    public String generateVerificationCode() {
        int codigo = random.nextInt(900000) + 100000;
        return String.valueOf(codigo);
    }


    @PostMapping("/checkCod")
    public ResponseEntity<Boolean> checkCod(@RequestBody Verification request) {
        String email = request.getEmail(); // Verifique se você está usando o email corretamente
        String cod = request.getCod();

        boolean codigoValido = verificarCodigo(cod);

        return ResponseEntity.ok(codigoValido);
    }

    private boolean verificarCodigo(String cod) {
        // Verificar se o código existe
        boolean codigoValido = verificationRepository.existsByCod(cod);

        if (codigoValido) {
            System.out.println("Codigo correto!");
        }

        return codigoValido;
    }




    @PutMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody RequestNewPassword request) {
        String email = request.email();
        String newPassword = request.password();

        boolean isChanged = userService.updatePassword(email, newPassword);

        if (isChanged) {
            return ResponseEntity.ok("Senha alterada com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }
    }
}