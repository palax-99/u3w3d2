package antoninopalazzolo.u3w3d1.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "prenotazioni")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Prenotazione {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;
    @Column(name = "data_richiesta", nullable = false)
    private LocalDate dataRichiesta;
    @Column
    private String note;
    @ManyToOne
    @JoinColumn(name = "dipendente_id")
    private Dipendente dipendente;
    @ManyToOne
    @JoinColumn(name = "viaggio_id")
    private Viaggio viaggio;

    public Prenotazione(LocalDate dataRichiesta, String note, Dipendente dipendente, Viaggio viaggio) {
        this.dataRichiesta = dataRichiesta;
        this.note = note;
        this.dipendente = dipendente;
        this.viaggio = viaggio;
    }
}
