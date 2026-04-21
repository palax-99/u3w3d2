package antoninopalazzolo.u3w3d1.payloads;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ViaggioDTO(
        @NotBlank(message = "Il campo non può rimanere vuoto!")
        @Size(min = 2, max = 10)
        String destinazione,
        @NotNull(message = "La data è obbligatorio")
        //Annotazione dove il campo è obbligatorio!!
        @FutureOrPresent
        //Data che deve essere o presente o in fututo
        LocalDate data,
        @Pattern(regexp = "IN_PROGRAMMA|COMPLETATO", message = "Lo stato deve essere IN_PROGRAMMA o COMPLETATO")
        // @Pattern permette di specificare i valori esatti accettati
        String stato) {
}
