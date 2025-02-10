package com.hotel.diamante.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTUtils {
    private static final long EXPIRATION_TIMEOUT   = 1000*60*24*7; // 7 dias
    private  final SecretKey SECRET;

    public JWTUtils() {
        String secreteString =  "771b7ba1998e9abda673a0e40a7f1d7040e50a627f79d96d7e64b397375fb05116e271325f21bda0f8fae90106b614be7a2c129a74e479fca07b3a42b48f4138f00bac5fe6f78a0bece286921e8a70c918e2d6ce6640cd2845e8ef55049c935367d0895549d16b74a51523cc990cd7ce1f53839ca658957a770be443e7001a07b211a26a481778e5501df3acc97e6c8c6b52bfab31b29ff21f2e4759d5deeb6f612e535ffff3f48b1db0a4667cd5232666dce8aa3da0b5c38cb26207a76df4c902cb2415a7b6172ce85965d1b34fbdd50eda3909664867428b7f25e9fe7c9dd40605928edcfce55cf8caacf16569b2edd8a168ee82fb55f774e9c36099e44400";
        byte[]keyBytes= Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
        this.SECRET=new SecretKeySpec(keyBytes,"HmacSHA256");

    }
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername()) // Set the subject (username)
                .issuedAt(new Date(System.currentTimeMillis())) // Set the issued date
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIMEOUT)) // Set the expiration date
                .signWith(SECRET) // Sign the token with the secret key
                .compact(); // Compact into a final token string
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }
    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(SECRET).build().parseSignedClaims(token).getPayload());
    }

    public boolean isValidateToken(String token, UserDetails userDetails){
        final String username= extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    private boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }
}
