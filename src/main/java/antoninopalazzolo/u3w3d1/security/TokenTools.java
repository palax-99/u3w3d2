package antoninopalazzolo.u3w3d1.security;

import antoninopalazzolo.u3w3d1.entities.Dipendente;
import antoninopalazzolo.u3w3d1.exceptions.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
// @Component dice a Spring di creare un'istanza di questa classe
// e tenerla disponibile per tutta l'applicazione
// è come @Service ma per classi di utilità che non sono Service
public class TokenTools {

    private final String secret;


    public TokenTools(@Value("${jwt.secret}") String secret) {
        // @Value legge il valore di jwt.secret dall'application.properties
        // che a sua volta lo prende dall'env.properties
        // questa è la chiave segreta con cui firmiamo i token
        // se la cambiamo, tutti i token esistenti diventano invalidi!
        this.secret = secret;
    }

    public String generateToken(Dipendente dipendente) {
        // Jwts viene dalla libreria jjwt-api che abbiamo aggiunto nel pom.xml
        // .builder() inizia la costruzione del token a catena
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                // issuedAt = quando è stato creato il token
                // System.currentTimeMillis() = timestamp attuale in millisecondi
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                // expiration = quando scade il token
                // 1000 = 1 secondo, *60 = 1 minuto, *60 = 1 ora, *24 = 1 giorno, *7 = 7 giorni
                // dopo 7 giorni il token non è più valido e il client deve rifare il login
                .subject(String.valueOf(dipendente.getId()))
                // subject = chi possiede il token
                // mettiamo l'id del dipendente per sapere chi sta facendo la richiesta
                // MAI mettere password o dati sensibili qui!
                // il payload del JWT non è cifrato, è solo codificato in Base64
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                // firmiamo il token con la chiave segreta usando l'algoritmo HMAC-SHA
                // questa firma garantisce che nessuno possa modificare il token
                // senza che il server se ne accorga
                .compact();
        // compact() genera la stringa finale del token
        // quella lunga stringa eyJ... che hai visto nelle slide
    }

    public void verifyToken(String token) {
        // questo metodo verifica che il token ricevuto sia valido
        try {
            Jwts.parser()
                    // parser() legge e analizza il token
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    // verifichiamo la firma con la stessa chiave segreta usata per generarlo
                    .build()
                    .parse(token);
            // parse() controlla tre cose:
            // 1. la firma — è stata modificata?
            // 2. la scadenza — è ancora valido?
            // 3. il formato — è un JWT corretto?
        } catch (Exception ex) {
            // se qualcosa non va lanciamo UnauthorizedException
            // il client riceverà un 401 e dovrà rifare il login
            throw new UnauthorizedException("Problemi col token! Effettua di nuovo il login!");
        }
    }

    public String extractIdFromToken(String token) {
        // parser() legge il token
        // getPayload() prende il payload
        // getSubject() estrae il subject — cioè l'id che abbiamo messo quando abbiamo generato il token
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
