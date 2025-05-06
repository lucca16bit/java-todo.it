package br.com.todoit.api.dto.tasks;

import br.com.todoit.api.entity.Priority;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateTaskDTO(
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

}
