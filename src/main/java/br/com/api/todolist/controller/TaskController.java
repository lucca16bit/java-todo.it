package br.com.api.todolist.controller;

import br.com.api.todolist.dto.tasks.CreateTaskDTO;
import br.com.api.todolist.dto.tasks.ListTaskDTO;
import br.com.api.todolist.dto.tasks.ViewTaskDTO;
import br.com.api.todolist.entity.Task;
import br.com.api.todolist.repository.TaskRepository;
import br.com.api.todolist.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository repository;

    @Autowired
    private TaskService service;

    @PostMapping
    @Transactional
    public ResponseEntity create(@RequestBody CreateTaskDTO dados, UriComponentsBuilder uriBuilder) {
        var task = new Task(dados);
        service.validate(task);
        repository.save(task);

        var uri = uriBuilder.path("/tasks/{id}")
                .buildAndExpand(task.getId()).toUri();

        return ResponseEntity.created(uri).body(new ViewTaskDTO(task));
    }

    @GetMapping
    public ResponseEntity<Page<ListTaskDTO>> list(@PageableDefault(size = 10, sort = {"startAt"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ListTaskDTO> tasks = repository.findAll(pageable)
                .map(ListTaskDTO::new);

        return ResponseEntity.ok(tasks);
    }
}
