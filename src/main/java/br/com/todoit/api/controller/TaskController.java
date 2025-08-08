package br.com.todoit.api.controller;

import br.com.todoit.api.dto.services.DateRangeDTO;
import br.com.todoit.api.dto.tasks.CreateTaskDTO;
import br.com.todoit.api.dto.tasks.ListTaskDTO;
import br.com.todoit.api.dto.tasks.UpdateTaskDTO;
import br.com.todoit.api.dto.tasks.ViewTaskDTO;
import br.com.todoit.api.entity.Task;
import br.com.todoit.api.entity.User;
import br.com.todoit.api.repository.TaskRepository;
import br.com.todoit.api.repository.UserRepository;
import br.com.todoit.api.service.TaskService;
import br.com.todoit.api.service.TokenService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@RestController
@RequestMapping("/tasks")
@SecurityRequirement(name = "bearer-key")
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
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
    public ResponseEntity<?> create(
            @RequestBody CreateTaskDTO data, UriComponentsBuilder uriBuilder,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userRepository.findByLogin(userDetails.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var task = new Task(data);
        task.setUser(user);

        service.validate(task);
        repository.save(task);

        var uri = uriBuilder.path("/tasks/{id}")
                .buildAndExpand(task.getId()).toUri();

        return ResponseEntity.created(uri).body(new ViewTaskDTO(task));
    }

    @GetMapping
    public ResponseEntity<Page<ListTaskDTO>> list(
            @PageableDefault(size = 10, sort = {"startAt"}, direction = Sort.Direction.ASC) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userRepository.findByLogin(userDetails.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Page<ListTaskDTO> tasks = repository.findByUserOrderByEndAtDesc(user, pageable)
                .map(ListTaskDTO::new);

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/today")
    public ResponseEntity<Page<ListTaskDTO>> listTodayTasks(
            @PageableDefault(size = 10, sort = {"startAt"}, direction = Sort.Direction.ASC) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userRepository.findByLogin(userDetails.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        DateRangeDTO todayRange = service.getTodayDate();
        LocalDateTime startOfDay = todayRange.start();
        LocalDateTime endOfDay = todayRange.end();

        Page<ListTaskDTO> tasks = repository.findByUserAndStartAtBetween(user, startOfDay, endOfDay, pageable)
                .map(ListTaskDTO::new);

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/tomorrow")
    public ResponseEntity<Page<ListTaskDTO>> listTomorrowTasks(
            @PageableDefault(size = 10, sort = {"startAt"}, direction = Sort.Direction.ASC) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userRepository.findByLogin(userDetails.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        DateRangeDTO tomorrow = service.getTomorrowDate();
        LocalDateTime startOfTomorrow = tomorrow.start();
        LocalDateTime endOfTomorrow = tomorrow.end();

        Page<ListTaskDTO> tasks = repository.findByUserAndStartAtBetween(user, startOfTomorrow, endOfTomorrow, pageable)
                .map(ListTaskDTO::new);

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable UUID id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userRepository.findByLogin(userDetails.getUsername());
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
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody UpdateTaskDTO data, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userRepository.findByLogin(userDetails.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var task = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));

        if (!task.getUser().equals(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para editar essa tarefa");
        }
        task.updateTask(data);

        var taskUpdated = repository.save(task);
        return ResponseEntity.ok().body(new ViewTaskDTO(taskUpdated));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable UUID id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userRepository.findByLogin(userDetails.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var task = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));

        if (!task.getUser().equals(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para editar essa tarefa");
        }

        repository.delete(task);

        return ResponseEntity.noContent().build();
    }
}
