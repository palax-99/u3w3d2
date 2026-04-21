package antoninopalazzolo.u3w3d1.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(UUID id) {
        super("Il record con id " + id + " non è stato trovato!");
    }
}
