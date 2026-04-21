package antoninopalazzolo.u3w3d1.payloads;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorsWithListDTO(
        List<String> error,
        String message,
        LocalDateTime timestamp) {
}
