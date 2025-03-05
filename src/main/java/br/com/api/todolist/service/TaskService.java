package br.com.api.todolist.service;

import br.com.api.todolist.entity.Task;
import br.com.api.todolist.exception.InvalidDateException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TaskService {
    public void validate(Task task) {
        if (task.getStartAt().isBefore(LocalDateTime.now())) {
            throw new InvalidDateException("A data de início e data de término deve ser maior do que a data atual.");
        }
        if (task.getStartAt().isAfter(task.getEndAt())) {
            throw  new InvalidDateException("A data de término deve ser maior do que a data de início");
        }
    }
}
