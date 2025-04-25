package br.com.api.todolist.dto.tasks;

import br.com.api.todolist.entity.Priority;
import br.com.api.todolist.entity.Task;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record ListTaskDTO(
        UUID id,

        String title,

        String description,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime startAt,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime endAt,

        Priority priority,

        Boolean completed
) {

    public ListTaskDTO(Task task) {
        this(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStartAt(),
                task.getEndAt(),
                task.getPriority(),
                task.getCompleted()
        );
    }
}
