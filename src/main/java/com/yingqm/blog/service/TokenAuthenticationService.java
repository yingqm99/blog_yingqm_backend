package com.yingqm.blog.service;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@Slf4j
//@Service
//public class TokenAuthenticationService {
//
//    @Value("${jwt.expiration_time}")
//    private long EXPIRATION_TIME;
//
//    @Value("${jwt.secretKey}")
//    private String secretKey;
//
//    private String tokenPrefix = "Bearer ";
//
//
//    public String generateJWT(String name) {
//        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
//        Map<String, Object> claims = new HashMap<String, Object>();
//        String jwt = tokenPrefix + Jwts.builder()
//                .setClaims(claims)
//                .setSubject(name)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(key).compact();
//        log.info(jwt);
//        return jwt;
//    }
//
//    public boolean verifyJWT(String JWT) {
//
//        String[] chunks = JWT.split("\\.");
//        String temp = decodeJWT(JWT);
//        log.info(temp);
//        return temp.equals(chunks[1]);
//
//    }
//
//    public String decodeJWT(String JWT) {
//        return Jwts.parserBuilder()
//                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
//                .build().parseClaimsJwt(JWT).getBody().getSubject();
//    }
//
//}