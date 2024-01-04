package com.example.todomanagement.service.impl;

import com.example.todomanagement.dto.RegisterDto;
import com.example.todomanagement.entity.Role;
import com.example.todomanagement.entity.User;
import com.example.todomanagement.exception.TodoAPIException;
import com.example.todomanagement.repository.RoleRepository;
import com.example.todomanagement.repository.UserRepository;
import com.example.todomanagement.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    @Override
    public String register(RegisterDto registerDto) {
        // check if username is already in database
        if(userRepository.existsByUsername(registerDto.getUsername())){
            throw new TodoAPIException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        // check if email is already in database
        if(userRepository.existsByEmail((registerDto.getEmail()))){
            throw new TodoAPIException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER");
        roles.add(userRole);

        user.setRoles(roles);

        userRepository.save(user);

        return "User Registered Successfully!";
    }
}
