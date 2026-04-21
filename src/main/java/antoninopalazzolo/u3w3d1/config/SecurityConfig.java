package antoninopalazzolo.u3w3d1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
// @Configuration = classe di configurazione
// @EnableWebSecurity = dice a Spring che questa classe
// customizza le impostazioni di Spring Security
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // Disabilitiamo le sessioni — usiamo JWT che è stateless
        // stateless = il server non ricorda niente tra una richiesta e l'altra
        // tutte le info necessarie sono nel token
        httpSecurity.sessionManagement(sessions ->
                sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Disabilitiamo il form di login di default di Spring Security
        // non ci serve — uso il mio endpoint /auth/login
        httpSecurity.formLogin(formLogin -> formLogin.disable());

        // Disabilitiamo la protezione CSRF
        // non serve con JWT e complicherebbe la vita al frontend
        httpSecurity.csrf(csrf -> csrf.disable());

        // Per ora permettiamo tutte le richieste
        // le proteggeremo con il TokenFilter
        httpSecurity.authorizeHttpRequests(req ->
                req.requestMatchers("/**").permitAll());

        return httpSecurity.build();
    }
}
