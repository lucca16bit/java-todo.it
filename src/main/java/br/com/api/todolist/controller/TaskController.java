package br.com.api.todolist.controller;

import br.com.api.todolist.dto.tasks.CreateTaskDTO;
import br.com.api.todolist.dto.tasks.ListTaskDTO;
import br.com.api.todolist.dto.tasks.UpdateTaskDTO;
import br.com.api.todolist.dto.tasks.ViewTaskDTO;
import br.com.api.todolist.entity.Task;
import br.com.api.todolist.entity.User;
import br.com.api.todolist.repository.TaskRepository;
import br.com.api.todolist.repository.UserRepository;
import br.com.api.todolist.service.TaskService;
import br.com.api.todolist.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskService service;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    @Transactional
    public ResponseEntity create(@RequestBody CreateTaskDTO dados, UriComponentsBuilder uriBuilder, Principal principal) {
        User user = (User) userRepository.findByLogin(principal.getName());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var task = new Task(dados);
        task.setUser(user);

        service.validate(task);
        repository.save(task);

        var uri = uriBuilder.path("/tasks/{id}")
                .buildAndExpand(task.getId()).toUri();

        return ResponseEntity.created(uri).body(new ViewTaskDTO(task));
    }

    @GetMapping
    public ResponseEntity<Page<ListTaskDTO>> list(@PageableDefault(size = 10, sort = {"startAt"}, direction = Sort.Direction.ASC) Pageable pageable,
                                                  Principal principal) {
        User user = (User) userRepository.findByLogin(principal.getName());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Page<ListTaskDTO> tasks = repository.findByUser(user, pageable)
                .map(ListTaskDTO::new);

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity view(@PathVariable UUID id, Principal principal) {
        User user = (User) userRepository.findByLogin(principal.getName());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var task = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Este ID não existe"));

        if (!task.getUser().equals(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para editar essa tarefa");
        }

        return ResponseEntity.ok(new ListTaskDTO(task));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity update(@PathVariable UUID id, @RequestBody UpdateTaskDTO update, Principal principal) {
        User user = (User) userRepository.findByLogin(principal.getName());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var task = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));

        if (!task.getUser().equals(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para editar essa tarefa");
        }
        task.updateTask(update);


        var taskUpdated = repository.save(task);
        return ResponseEntity.ok().body(new ViewTaskDTO(taskUpdated));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable UUID id, Principal principal) {
        User user = (User) userRepository.findByLogin(principal.getName());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var task = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));

        if (!task.getUser().equals(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para editar essa tarefa");
        }

        service.delete(task);

        return ResponseEntity.noContent().build();
    }
}
