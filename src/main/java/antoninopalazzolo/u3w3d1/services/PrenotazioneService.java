package antoninopalazzolo.u3w3d1.services;

import antoninopalazzolo.u3w3d1.entities.Dipendente;
import antoninopalazzolo.u3w3d1.entities.Prenotazione;
import antoninopalazzolo.u3w3d1.entities.Viaggio;
import antoninopalazzolo.u3w3d1.exceptions.BadRequestException;
import antoninopalazzolo.u3w3d1.exceptions.NotFoundException;
import antoninopalazzolo.u3w3d1.payloads.PrenotazioneDTO;
import antoninopalazzolo.u3w3d1.repositories.PrenotazioneRepository;
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
public class PrenotazioneService {
    private final DipendenteService dipendenteService;
    private final ViaggioService viaggioService;
    private final PrenotazioneRepository prenotazioneRepository;

    @Autowired
    public PrenotazioneService(DipendenteService dipendenteService,
                               ViaggioService viaggioService,
                               PrenotazioneRepository prenotazioneRepository) {
        this.dipendenteService = dipendenteService;
        this.viaggioService = viaggioService;
        this.prenotazioneRepository = prenotazioneRepository;
    }

    public Prenotazione savePrenotazione(PrenotazioneDTO body) {
        Dipendente dipendente = this.dipendenteService.findByIdDipendente(body.dipendenteId());
        Viaggio viaggio = this.viaggioService.findById(body.viaggioId());
        // Ho la possibilità di cercare sia il dipendente che il viaggio con i metodi
        // perchè mi sono iniettato i loro service
        if (this.prenotazioneRepository.existsByDipendenteAndDataRichiesta(dipendente, body.dataRichiesta()))
            throw new BadRequestException("Il dipendente ha già una prenotazione per il " + body.dataRichiesta());
        //Controllo se esiste già unn dipendente che ha un aprenotazione per quella data, mamma mia che esercizio prof, lunghissimo ahahah
        Prenotazione nuovaPrenotazione = new Prenotazione(body.dataRichiesta(), body.note(), dipendente, viaggio);
        Prenotazione salvata = this.prenotazioneRepository.save(nuovaPrenotazione);
        log.info("Prenotazione con id " + salvata.getId() + " salvata correttamente!");
        return salvata;
    }

    public Page<Prenotazione> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.prenotazioneRepository.findAll(pageable);
    }

    public Prenotazione findById(UUID id) {
        Prenotazione found = this.prenotazioneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
        log.info("Prenotazione con id " + id + " trovata!");
        return found;
    }

    public Prenotazione findByIdAndUpdate(UUID id, PrenotazioneDTO body) {
        Prenotazione found = this.findById(id);

        Dipendente dipendente = this.dipendenteService.findByIdDipendente(body.dipendenteId());
        Viaggio viaggio = this.viaggioService.findById(body.viaggioId());

        // Controllo duplicato solo se cambio la data o il dipendente
        if (!found.getDataRichiesta().equals(body.dataRichiesta()) ||
                !found.getDipendente().getId().equals(body.dipendenteId())) {
            if (this.prenotazioneRepository.existsByDipendenteAndDataRichiesta(dipendente, body.dataRichiesta()))
                throw new BadRequestException("Il dipendente ha già una prenotazione per il " + body.dataRichiesta());
        }

        found.setDataRichiesta(body.dataRichiesta());
        found.setNote(body.note());
        found.setDipendente(dipendente);
        found.setViaggio(viaggio);

        log.info("Prenotazione con id " + id + " aggiornata!");
        return this.prenotazioneRepository.save(found);
    }

    public void findByIdAndDelete(UUID id) {
        Prenotazione found = this.findById(id);
        this.prenotazioneRepository.delete(found);
        log.info("Prenotazione con id " + id + " eliminata!");
    }
}
