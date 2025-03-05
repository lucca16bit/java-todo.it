package br.com.api.todolist.dto.tasks;

import br.com.api.todolist.entity.Priority;
import br.com.api.todolist.entity.Task;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ListTaskDTO(
        String title,

        String description,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime startAt,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime endAt,

        Priority priority
) {

    public ListTaskDTO(Task task) {
        this(
                task.getTitle(),
                task.getDescription(),
                task.getStartAt(),
                task.getEndAt(),
                task.getPriority()
        );
    }
}
