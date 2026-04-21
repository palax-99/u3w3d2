package antoninopalazzolo.u3w3d1.payloads;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.UUID;

public record PrenotazioneDTO(
        @NotNull(message = "La data di richiesta deve essere obbligatoria!")
        @PastOrPresent
        // può essere stata richiesta nel passato o presente!
        LocalDate dataRichiesta,
        String note,
        @NotNull(message = "Il dipendente è obbligatorio!")
        //non passo direttamente l'oggetto ma solo l'id
        UUID dipendenteId,
        @NotNull(message = "Il viaggio è obbligatorio!")
        //non passo direttamente l'oggetto ma solo l'id
        UUID viaggioId) {
}
