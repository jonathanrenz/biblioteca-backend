package jonathanrenz.biblioteca.infra.services;

import jonathanrenz.biblioteca.domain.User;
import jonathanrenz.biblioteca.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean updatePassword(String email, String newPassword) {
        // Verifica se a nova senha é nula ou vazia
        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("A nova senha não pode ser nula ou vazia.");
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }

        return false;
    }

}

