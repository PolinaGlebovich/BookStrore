package com.example.market.controller;

import com.example.market.configuration.JWTTokenProvider;
import com.example.market.dto.AuthRequestDto;
import com.example.market.dto.UserDto;
import com.example.market.entity.User;
import com.example.market.mapper.UserMapper;
import com.example.market.repository.UserRepository;
import com.example.market.service.UserService;
import com.example.market.service.impl.CustomUserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final AuthenticationManager authenticationManager;

    private final JWTTokenProvider jwtTokenProvider;


    @PostMapping("/register")
    public ResponseEntity<User> registration(@RequestBody UserDto dto){
        User userDtoToUser = userMapper.userDtoToUser(dto);
        User user = userService.save(userDtoToUser);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetailsImpl userDetails = (CustomUserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtTokenProvider.generateToken(
                userDetails.getUsername(),
                userDetails.getId(),
                userDetails.getAuthorities());

        return ResponseEntity.ok(jwt);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<User> findUserById (@PathVariable Long id) {
        User user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/findByUsername/{username}")
    public ResponseEntity<User> findUserByUsername (@PathVariable String username) {
        User user = (User) userService.loadUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<User>> findAll() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/updateUser/{id}")
    public User updateUser(@RequestBody UserDto dto,
                           @PathVariable Long id) {
        return userRepository.findUserById(id)
                .map(user -> {
                    user.setName(dto.getName());
                    user.setUsername(dto.getUsername());
                    user.setPassword(dto.getPassword());
                    return userService.save(user);
                }).orElseThrow();
    }

    @DeleteMapping("/deleteUserById/{id}")
    public HttpStatus delete (@PathVariable Long id) {
         userService.delete(id);
        return HttpStatus.OK;
    }
}
