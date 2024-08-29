package jonathanrenz.biblioteca.repositories;

import jonathanrenz.biblioteca.domain.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {
    boolean existsByCod(String cod);
}

