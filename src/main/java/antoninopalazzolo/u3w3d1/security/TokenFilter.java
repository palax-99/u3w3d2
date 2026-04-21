package antoninopalazzolo.u3w3d1.security;

import antoninopalazzolo.u3w3d1.entities.Dipendente;
import antoninopalazzolo.u3w3d1.exceptions.UnauthorizedException;
import antoninopalazzolo.u3w3d1.services.DipendenteService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private final TokenTools tokenTools;
    private final DipendenteService dipendenteService;

    public TokenFilter(TokenTools tokenTools, DipendenteService dipendenteService) {
        this.tokenTools = tokenTools;
        this.dipendenteService = dipendenteService;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Inserire il token nell'authorization header nel formato corretto");

        String accessToken = authHeader.replace("Bearer ", "");

        // 1. Verifico che il token sia valido
        tokenTools.verifyToken(accessToken);

        // 2. Estraggo l'id dell'utente dal token
        String id = tokenTools.extractIdFromToken(accessToken);

        // 3. Cerco l'utente nel database tramite l'id
        Dipendente dipendente = dipendenteService.findByIdDipendente(UUID.fromString(id));

        // 4. Salvo l'utente nel SecurityContext
        // Questo è quello che permette a @AuthenticationPrincipal di funzionare
        // Spring legge da qui chi sta facendo la richiesta
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                dipendente, null, dipendente.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 5. Passo al prossimo filtro o al Controller
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
