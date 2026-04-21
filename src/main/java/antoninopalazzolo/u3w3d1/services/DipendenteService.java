package antoninopalazzolo.u3w3d1.services;

import antoninopalazzolo.u3w3d1.entities.Dipendente;
import antoninopalazzolo.u3w3d1.exceptions.BadRequestException;
import antoninopalazzolo.u3w3d1.exceptions.NotFoundException;
import antoninopalazzolo.u3w3d1.payloads.DipendenteDTO;
import antoninopalazzolo.u3w3d1.repositories.DipendenteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class DipendenteService {
    private final DipendenteRepository dipendenteRepository;

    @Autowired // Constructor injection, potevo farea anche la field volendo ma è sempre meglio usare questa
    public DipendenteService(DipendenteRepository dipendenteRepository) {
        // aggiungo cloudinaryConfig nel costruttore
        this.dipendenteRepository = dipendenteRepository;
    }

    public Dipendente saveDipendente(DipendenteDTO body) {
        if (this.dipendenteRepository.existsByEmail(body.email()))
            throw new BadRequestException("L'indirizzo email" + body.email() + "è già in uso");
        if (this.dipendenteRepository.existsByUsername(body.username()))
            throw new BadRequestException(("L'username" + body.username() + "è già in uso"));
        Dipendente newDipendente = new Dipendente(body.username(), body.name(), body.surname(), body.email(), body.password());
        Dipendente dipendenteSalvato = this.dipendenteRepository.save(newDipendente);
        log.info("Il dipendente con id " + dipendenteSalvato.getId() + " è stato salvato correttamente!");
        return dipendenteSalvato;
    }

    public Page<Dipendente> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.dipendenteRepository.findAll(pageable);
    }

    public Dipendente findByIdDipendente(UUID id) {
        Dipendente found = this.dipendenteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
        log.info("Dipendente con id " + id + " trovato!");
        return found;
    }

    public Dipendente findByIdAndUpdate(UUID id, DipendenteDTO body) {
        Dipendente found = this.findByIdDipendente(id);
        if (!found.getEmail().equals(body.email()) && this.dipendenteRepository.existsByEmail(body.email()))
            throw new BadRequestException("L'email " + body.email() + " è già in uso!");

        // Controllo username solo se sto cambiando username
        if (!found.getUsername().equals(body.username()) && this.dipendenteRepository.existsByUsername(body.username()))
            throw new BadRequestException("L'username " + body.username() + " è già in uso!");

        // Aggiorno i campi
        found.setUsername(body.username());
        found.setName(body.name());
        found.setSurname(body.surname());
        found.setEmail(body.email());
        found.setAvatar("https://ui-avatars.com/api?name=" + body.name() + "+" + body.surname());

        log.info("Dipendente con id " + id + " aggiornato!");
        return this.dipendenteRepository.save(found);
    }

    public void findByIdAndDelete(UUID id) {
        // Trovo il dipendente, se non esiste lancio NotFoundException
        Dipendente found = this.findByIdDipendente(id);
        this.dipendenteRepository.delete(found);
        log.info("Dipendente con id " + id + " eliminato!");
    }

    public Dipendente findByEmail(String email) {
        return this.dipendenteRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Dipendente con email " + email + " non trovato!"));
    }

}

