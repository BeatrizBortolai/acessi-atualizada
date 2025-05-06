package org.acme.entities;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    private static final String SEGREDO = "minhaChaveSuperSecretaParaJwtComTamanhoAdequado!";
    private static final long EXPIRACAO = 1000L * 60 * 60 * 24 * 7; // 1 hora

    private static final Key CHAVE = Keys.hmacShaKeyFor(SEGREDO.getBytes());

    public static String gerarToken(String login) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(login)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRACAO))
                .signWith(CHAVE, SignatureAlgorithm.HS256);

        return builder.compact();
    }

    public static String validarToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token inválido ou mal formatado");
        }
        String token = authHeader.substring("Bearer ".length());

        return Jwts.parserBuilder()
                .setSigningKey(CHAVE)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // Aqui retorna o ID como String
    }

}

