package com.fatec.candidatura.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.List;

/**
 * O gateway já valida o token. Aqui só descobrimos QUEM é o usuário  e o perfil ,
 * usando a mesma chave do login e do gateway .
 */
@Component
public class JwtLeitor {
    private static final String SECRET = "u7x!A$9zLq#2vNf@eR6tYpWmZcXbGdQh";

    private Claims ler(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new NaoAutenticadoException("Token não informado");
        }
        try {
            SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(authorization.substring(7))
                    .getPayload();
        } catch (JwtException | IllegalArgumentException ex) {
            throw new NaoAutenticadoException("Token inválido ou expirado");
        }
    }

    public String getUsername(String authorization) {
        return ler(authorization).getSubject();
    }

    /** Segunda barreira: além do gateway (/fatec/admin/**), o próprio serviço confere o perfil ADMIN. */
    public String exigirAdmin(String authorization) {
        Claims claims = ler(authorization);
        List<?> roles = claims.get("roles", List.class);
        if (roles == null || !roles.contains("ROLE_ADMIN")) {
            throw new AcessoNegadoException("Somente o administrador pode ver o relatório");
        }
        return claims.getSubject();
    }
}
