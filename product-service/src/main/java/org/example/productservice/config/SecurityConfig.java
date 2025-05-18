package org.example.productservice.config;

import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    // бин для проверки подписи JWT (тот же секрет, что генерит user‑service)
    @Bean
    public JwtDecoder jwtDecoder(@Value("${jwt.secret}") String base64secret) {
        byte[] keyBytes = Decoders.BASE64.decode(base64secret);
        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    // основной фильтр‑цепочка для сервлетов
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // отключаем CSRF, т.к. у нас stateless REST
                .csrf(csrf -> csrf.disable())

                // без сессий
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // какие пути защищаем
                .authorizeHttpRequests(auth -> auth
                        // все запросы к /products/** должны идти с JWT
                        .requestMatchers("/products/**").authenticated()
                        // всё остальное — без проверки
                        .anyRequest().permitAll()
                )

                // подключаем Resource Server для JWT
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(withDefaults())
                );

        return http.build();
    }
}