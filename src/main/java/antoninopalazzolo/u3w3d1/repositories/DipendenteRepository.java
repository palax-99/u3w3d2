package antoninopalazzolo.u3w3d1.repositories;

import antoninopalazzolo.u3w3d1.entities.Dipendente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DipendenteRepository extends JpaRepository<Dipendente, UUID> {
    // Controllo se esiste già un dipendente con questa email
    // Usato nel Service prima di salvare per evitare duplicati
    // Restituisce true/false — non ho bisogno dell'oggetto
    boolean existsByEmail(String email);

    // Controlla se esiste già un dipendente con questo username
    // Stesso ragionamento dell'email — username deve essere unico
    boolean existsByUsername(String username);

    Optional<Dipendente> findByEmail(String email);
}
