package antoninopalazzolo.u3w3d1.services;

import antoninopalazzolo.u3w3d1.entities.StatoViaggio;
import antoninopalazzolo.u3w3d1.entities.Viaggio;
import antoninopalazzolo.u3w3d1.exceptions.NotFoundException;
import antoninopalazzolo.u3w3d1.payloads.ViaggioDTO;
import antoninopalazzolo.u3w3d1.repositories.ViaggioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ViaggioService {
    private final ViaggioRepository viaggioRepository;

    public ViaggioService(ViaggioRepository viaggioRepository) {
        this.viaggioRepository = viaggioRepository;
    }

    public Viaggio save(ViaggioDTO body) {
        // Converto lo stato da String a enum
        Viaggio newViaggio = new Viaggio(body.destinazione(), body.data(), StatoViaggio.valueOf(body.stato()));
        log.info("Viaggio salvato correttamente!");
        return this.viaggioRepository.save(newViaggio);
    }

    public Page<Viaggio> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.viaggioRepository.findAll(pageable);
    }

    public Viaggio findById(UUID id) {
        Viaggio found = this.viaggioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
        log.info("Viaggio con id " + id + " trovato!");
        return found;
    }

    public Viaggio findByIdAndUpdate(UUID id, ViaggioDTO body) {
        Viaggio found = this.findById(id);
        found.setDestinazione(body.destinazione());
        found.setData(body.data());
        found.setStato(StatoViaggio.valueOf(body.stato()));
        log.info("Viaggio con id " + id + " aggiornato!");
        return this.viaggioRepository.save(found);
    }

    public void findByIdAndDelete(UUID id) {
        Viaggio found = this.findById(id);
        this.viaggioRepository.delete(found);
        log.info("Viaggio con id " + id + " eliminato!");
    }

    // Metodo speciale per cambiare solo lo stato
    public Viaggio cambiaStato(UUID id, String stato) {
        Viaggio found = this.findById(id);
        found.setStato(StatoViaggio.valueOf(stato));
        log.info("Stato del viaggio " + id + " cambiato in " + stato);
        return this.viaggioRepository.save(found);
    }
}

