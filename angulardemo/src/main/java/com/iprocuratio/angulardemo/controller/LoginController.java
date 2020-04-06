package com.iprocuratio.angulardemo.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.iprocuratio.angulardemo.model.User;
import com.iprocuratio.angulardemo.security.JWTAuthorizationFilter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/login")
public class LoginController {

    // Recupera el JWT
    @PostMapping("/")
    public User login(@RequestParam("user") String username, @RequestParam("password") String pwd) {
        String token = getJWTToken(username);
        User user = new User();
        user.setUser(username);
        user.setToken(token);
        return user;

    }

    private String getJWTToken(String username) {
        String secretKey = JWTAuthorizationFilter.SECRET;
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts.builder().setId("softtekJWT").setSubject(username)
                .claim("authorities", grantedAuthorities.stream().map((Object o) -> {
                    return ((GrantedAuthority) o).getAuthority();
                }).collect(Collectors.toList())).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();
        return "Bearer " + token;
    }

}