package antoninopalazzolo.u3w3d1.services;

import antoninopalazzolo.u3w3d1.entities.Dipendente;
import antoninopalazzolo.u3w3d1.exceptions.NotFoundException;
import antoninopalazzolo.u3w3d1.exceptions.UnauthorizedException;
import antoninopalazzolo.u3w3d1.payloads.LoginDTO;
import antoninopalazzolo.u3w3d1.security.TokenTools;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final DipendenteService dipendenteService;
    private final TokenTools tokenTools;
    private final PasswordEncoder bcrypt;

    public AuthService(DipendenteService dipendenteService, TokenTools tokenTools, PasswordEncoder bcrypt) {
        this.dipendenteService = dipendenteService;
        this.tokenTools = tokenTools;
        this.bcrypt = bcrypt;
    }

    public String checkCredentialsAndGenerateToken(LoginDTO body) {
        try {
            Dipendente found = this.dipendenteService.findByEmail(body.email());
            if (this.bcrypt.matches(body.password(), found.getPassword())) {
                return this.tokenTools.generateToken(found);

            } else {
                throw new UnauthorizedException("Credenziali errate");
            }
        } catch (NotFoundException ex) {
            throw new UnauthorizedException("Credenziali errate");
        }
    }
}
