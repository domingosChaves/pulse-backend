package com.domingos.pulse_backend.security;

import com.domingos.pulse_backend.security.dto.TokenResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private final JwtProperties props;
    private final SecretKey key;

    public JwtService(JwtProperties props) {
        this.props = props;
        // Permite chave como texto simples ou Base64
        SecretKey k;
        String secret = props.getSecret();
        if (secret == null || secret.isBlank()) {
            // fallback para evitar NPE em ambientes sem configuração
            secret = "change-me-change-me-change-me-change-me-32";
        }
        try {
            k = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        } catch (Exception e) {
            k = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }
        this.key = k;
    }

    public String generateAccessToken(Usuario u) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", u.getRoles());
        claims.put("status", u.getStatus().name());
        claims.put("provedor", u.getProvedor());
        claims.put("typ", "access");
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(props.getAccessTtl() > 0 ? props.getAccessTtl() : 900); // 15min default
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(u.getUsername())
                .setIssuer(props.getIssuer())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Usuario u) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("status", u.getStatus().name());
        claims.put("provedor", u.getProvedor());
        claims.put("typ", "refresh");
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(props.getRefreshTtl() > 0 ? props.getRefreshTtl() : 2592000); // 30d default
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(u.getUsername())
                .setIssuer(props.getIssuer())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) throws JwtException {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    public boolean isRefreshToken(Jws<Claims> jws) {
        Object typ = jws.getBody().get("typ");
        return typ != null && "refresh".equals(typ.toString());
    }

    public TokenResponse tokensFor(Usuario u) {
        String access = generateAccessToken(u);
        String refresh = generateRefreshToken(u);
        long expiresIn = props.getAccessTtl() > 0 ? props.getAccessTtl() : 900;
        return new TokenResponse(access, refresh, expiresIn);
    }
}
