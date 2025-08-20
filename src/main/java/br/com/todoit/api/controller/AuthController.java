package br.com.todoit.api.controller;

import br.com.todoit.api.dto.auth.CreateUserDTO;
import br.com.todoit.api.dto.auth.LoginUserDTO;
import br.com.todoit.api.dto.auth.TokenDTO;
import br.com.todoit.api.entity.User;
import br.com.todoit.api.repository.UserRepository;
import br.com.todoit.api.service.TokenService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@SecurityRequirement(name = "bearer-key")
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
    public ResponseEntity<Map<String, String>> register(@RequestBody @Valid CreateUserDTO data) {
        Map<String, String> response = new HashMap<>();

        if (repository.findByLogin(data.login()) == null) {
            String password = passwordEncoder.encode(data.password());

            User userCreated = new User(null, data.name(), data.login(), password, null);
            repository.save(userCreated);

            response.put("message", "Usuário cadastrado");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response.put("message", "Usuário já existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginUserDTO data) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        Authentication authentication = manager.authenticate(authenticationToken);

        String token = tokenService.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new TokenDTO(token));
    }
}
