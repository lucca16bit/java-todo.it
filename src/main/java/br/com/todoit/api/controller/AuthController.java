package br.com.todoit.api.controller;

import br.com.todoit.api.dto.users.CreateUserDTO;
import br.com.todoit.api.dto.users.LoginUserDTO;
import br.com.todoit.api.dto.users.TokenDTO;
import br.com.todoit.api.entity.User;
import br.com.todoit.api.repository.UserRepository;
import br.com.todoit.api.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @Transactional
    public ResponseEntity register(@RequestBody @Valid CreateUserDTO createUser) {
        if (repository.findByLogin(createUser.login()) == null) {
            String password = passwordEncoder.encode(createUser.password());

            User userCreated = new User(null, createUser.name(), createUser.login(), password, null);
            repository.save(userCreated);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Usuário cadastrado"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Usuário já existe"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginUserDTO loginUser) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginUser.login(), loginUser.password());
        var authentication = manager.authenticate(authenticationToken);

        var token = tokenService.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new TokenDTO(token));
    }
}
