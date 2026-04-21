package antoninopalazzolo.u3w3d1.controllers;

import antoninopalazzolo.u3w3d1.entities.Dipendente;
import antoninopalazzolo.u3w3d1.exceptions.BadRequestException;
import antoninopalazzolo.u3w3d1.payloads.DipendenteDTO;
import antoninopalazzolo.u3w3d1.services.DipendenteService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/dipendenti")
public class DipendenteController {
    private final DipendenteService dipendenteService;

    public DipendenteController(DipendenteService dipendenteService) {
        this.dipendenteService = dipendenteService;
    }

    @GetMapping("/me")
    public Dipendente getMe(@AuthenticationPrincipal Dipendente currentDipendente) {
        // @AuthenticationPrincipal inietta automaticamente il dipendente
        // che ha fatto la richiesta — Spring lo legge dal SecurityContext
        // che abbiamo popolato nel TokenFilter
        return currentDipendente;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Dipendente saveDipendente(@RequestBody @Validated DipendenteDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            // Estraggo solo i messaggi di errore in una lista pulita
            List<String> errors = validation.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new BadRequestException(errors.toString());
        }
        return dipendenteService.saveDipendente(body);
    }

    @GetMapping
    public Page<Dipendente> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        return dipendenteService.findAll(page, size, sortBy);
    }


    @PutMapping("/{id}")
    public Dipendente findByIdAndUpdate(@PathVariable UUID id,
                                        @RequestBody @Validated DipendenteDTO body,
                                        BindingResult validation) {
        if (validation.hasErrors()) {
            List<String> errors = validation.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new BadRequestException(errors.toString());
        }
        return dipendenteService.findByIdAndUpdate(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void findByIdAndDelete(@PathVariable UUID id) {
        dipendenteService.findByIdAndDelete(id);
    }

    @PutMapping("/me")
    public Dipendente updateMe(@AuthenticationPrincipal Dipendente currentDipendente,
                               @RequestBody @Validated DipendenteDTO body,
                               BindingResult validation) {
        if (validation.hasErrors()) {
            List<String> errors = validation.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new BadRequestException(errors.toString());
        }
        // uso l'id del dipendente loggato — non ho bisogno del PathVariable!
        return dipendenteService.findByIdAndUpdate(currentDipendente.getId(), body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMe(@AuthenticationPrincipal Dipendente currentDipendente) {
        // cancello il dipendente loggato
        dipendenteService.findByIdAndDelete(currentDipendente.getId());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
// solo gli ADMIN possono vedere il profilo di un altro dipendente
    public Dipendente findById(@PathVariable UUID id) {
        return dipendenteService.findByIdDipendente(id);
    }


}
