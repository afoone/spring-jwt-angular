package com.iprocuratio.angulardemo.controller;

import java.util.List;

import com.iprocuratio.angulardemo.model.User;
import com.iprocuratio.angulardemo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping("/")
    public List<User> getUsuarios() {
        return userRepository.findAll();
    }




}