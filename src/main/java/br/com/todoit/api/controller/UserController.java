package br.com.todoit.api.controller;

import br.com.todoit.api.dto.user.UpdateUserDTO;
import br.com.todoit.api.dto.user.ViewUserDTO;
import br.com.todoit.api.entity.User;
import br.com.todoit.api.repository.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "bearer-key")
public class UserController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/me")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) repository.findByLogin(userDetails.getUsername());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        ViewUserDTO data = new ViewUserDTO(user.getName(), user.getLogin());

        return ResponseEntity.ok().body(data);
    }

    @PutMapping("/me")
    @Transactional
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody UpdateUserDTO data) {
        User user = (User) repository.findByLogin(userDetails.getUsername());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        user.updateUser(new UpdateUserDTO(data.name(),data.login(),passwordEncoder.encode(data.password())));

        return ResponseEntity.ok().body(new ViewUserDTO(user.getName(), user.getLogin()));
    }

    @DeleteMapping("/me")
    @Transactional
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) repository.findByLogin(userDetails.getUsername());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        repository.delete(user);

        return ResponseEntity.ok().body("Usuário deletado.");
    }
}
