package antoninopalazzolo.u3w3d1.services;

import antoninopalazzolo.u3w3d1.entities.Dipendente;
import antoninopalazzolo.u3w3d1.exceptions.NotFoundException;
import antoninopalazzolo.u3w3d1.exceptions.UnauthorizedException;
import antoninopalazzolo.u3w3d1.payloads.LoginDTO;
import antoninopalazzolo.u3w3d1.security.TokenTools;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final DipendenteService dipendenteService;
    private final TokenTools tokenTools;

    public AuthService(DipendenteService dipendenteService, TokenTools tokenTools) {
        this.dipendenteService = dipendenteService;
        this.tokenTools = tokenTools;
    }

    public String checkCredentialsAndGenerateToken(LoginDTO body) {
        // 1. Cerco il dipendente per email
        // se non esiste → NotFoundException → la catturo e lancio UnauthorizedException
        // non voglio dire al client se l'email esiste o no — motivo di sicurezza!
        try {
            Dipendente found = this.dipendenteService.findByEmail(body.email());

            // 2. Controllo se la password corrisponde
            if (found.getPassword().equals(body.password())) {
                // 3. Credenziali ok → genero il token e lo ritorno
                return this.tokenTools.generateToken(found);
            } else {
                // 4. Password sbagliata → UnauthorizedException
                throw new UnauthorizedException("Credenziali errate");
            }
        } catch (NotFoundException ex) {
            // Email non trovata → stessa eccezione della password sbagliata
            // non diciamo mai se è l'email o la password ad essere sbagliata!
            throw new UnauthorizedException("Credenziali errate");
        }
    }
}
