package br.com.api.todolist.controller;

import br.com.api.todolist.dto.tasks.CreateTaskDTO;
import br.com.api.todolist.dto.tasks.ViewTaskDTO;
import br.com.api.todolist.entity.Task;
import br.com.api.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity create(@RequestBody CreateTaskDTO dados, UriComponentsBuilder uriBuilder) {
        var task = new Task(dados);
        repository.save(task);

        var uri = uriBuilder.path("/tasks/{id}")
                .buildAndExpand(task.getId()).toUri();

        return ResponseEntity.created(uri).body(new ViewTaskDTO(task));
    }
}
