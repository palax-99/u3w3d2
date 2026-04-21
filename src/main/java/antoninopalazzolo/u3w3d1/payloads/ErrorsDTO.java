package antoninopalazzolo.u3w3d1.payloads;

import java.time.LocalDateTime;

public record ErrorsDTO(
        String message,
        LocalDateTime timestamp

) {
}
