package br.com.todoit.api.dto.tasks;

import br.com.todoit.api.entity.Priority;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CreateTaskDTO(
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
