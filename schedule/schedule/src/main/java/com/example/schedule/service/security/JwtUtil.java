package com.example.schedule.service.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "my_secret_key"; // put in env in production
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        long now = System.currentTimeMillis();
        long exp = now + EXPIRATION_TIME;

        String header = Base64.getUrlEncoder().withoutPadding()
                .encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8));

        String payload = String.format("{\"sub\":\"%s\",\"iat\":%d,\"exp\":%d}",
                userPrincipal.getUsername(), now / 1000, exp / 1000);

        String base64Payload = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(payload.getBytes(StandardCharsets.UTF_8));

        String signature = hmacSha256(header + "." + base64Payload, SECRET_KEY);

        return header + "." + base64Payload + "." + signature;
    }

    public String extractUsername(String token) {
        String[] parts = token.split("\\.");
        if (parts.length < 2) return null;

        String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        // naive parsing, in production use JSON library
        String subField = "\"sub\":\"";
        int start = payloadJson.indexOf(subField) + subField.length();
        int end = payloadJson.indexOf("\"", start);
        return payloadJson.substring(start, end);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username != null && username.equals(userDetails.getUsername()) && !isExpired(token);
    }

    private boolean isExpired(String token) {
        try {
            String[] parts = token.split("\\.");
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            String expField = "\"exp\":";
            int start = payloadJson.indexOf(expField) + expField.length();
            int end = payloadJson.indexOf("}", start);
            long exp = Long.parseLong(payloadJson.substring(start, end));
            return exp < (System.currentTimeMillis() / 1000);
        } catch (Exception e) {
            return true;
        }
    }

    private String hmacSha256(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate HMAC", e);
        }
    }
}
