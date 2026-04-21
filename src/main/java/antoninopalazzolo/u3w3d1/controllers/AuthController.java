package antoninopalazzolo.u3w3d1.controllers;

import antoninopalazzolo.u3w3d1.entities.Dipendente;
import antoninopalazzolo.u3w3d1.exceptions.BadRequestException;
import antoninopalazzolo.u3w3d1.payloads.DipendenteDTO;
import antoninopalazzolo.u3w3d1.payloads.LoginDTO;
import antoninopalazzolo.u3w3d1.payloads.LoginRespDTO;
import antoninopalazzolo.u3w3d1.services.AuthService;
import antoninopalazzolo.u3w3d1.services.DipendenteService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final DipendenteService dipendenteService;

    public AuthController(AuthService authService, DipendenteService dipendenteService) {
        this.authService = authService;
        this.dipendenteService = dipendenteService;
    }

    @PostMapping("/login")
    public LoginRespDTO login(@RequestBody LoginDTO body) {
        return new LoginRespDTO(this.authService.checkCredentialsAndGenerateToken(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Dipendente register(@RequestBody @Validated DipendenteDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            List<String> errors = validation.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new BadRequestException(errors.toString());
        }
        return dipendenteService.saveDipendente(body);
    }
}
