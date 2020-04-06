package com.iprocuratio.angulardemo.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.iprocuratio.angulardemo.model.User;
import com.iprocuratio.angulardemo.repository.UserRepository;
import com.iprocuratio.angulardemo.security.JWTAuthorizationFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
public class LoginController {

    @Autowired
    UserRepository userRepository;

    Logger l = LoggerFactory.getLogger(this.getClass());

    // Recupera el JWT
    @PostMapping("/login/")
    public User login(@RequestParam("user") String username, @RequestParam("password") String pwd) {
        l.debug("entrando en login");

        l.debug(username);
        List<User> users = userRepository.findByUser(username);
        l.debug(users.toString());
        if (users.size() < 1)
            return null;
        User user = users.get(0);
        // user.setUser(username);
        if (!user.getPassword().equals(pwd))
            return null;
        String token = getJWTToken(username);
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