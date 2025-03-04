package br.com.api.todolist.entity;

import br.com.api.todolist.dto.tasks.CreateTaskDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Entity(name = "tb_tasks")
public class Task {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(length = 50, nullable = false)
    private String title;

    private String description;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    public Task(CreateTaskDTO dados) {
        this.title = dados.title();
        this.description = dados.description();
        this.startAt = dados.startAt();
        this.endAt = dados.endAt();
        this.priority = dados.priority();
    }

    public void setTitle(String title) throws Exception{
        if (title.length() > 50) {
            throw new Exception("O campo deve contér no máximo 50 caracteres");
        }
        this.title = title;
    }
}
