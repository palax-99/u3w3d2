package antoninopalazzolo.u3w3d1.controllers;

import antoninopalazzolo.u3w3d1.entities.Dipendente;
import antoninopalazzolo.u3w3d1.exceptions.BadRequestException;
import antoninopalazzolo.u3w3d1.payloads.DipendenteDTO;
import antoninopalazzolo.u3w3d1.services.DipendenteService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/{id}")
    public Dipendente findById(@PathVariable UUID id) {
        return dipendenteService.findByIdDipendente(id);
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


}
