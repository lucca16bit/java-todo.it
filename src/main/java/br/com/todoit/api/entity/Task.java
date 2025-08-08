package br.com.todoit.api.entity;

import br.com.todoit.api.dto.tasks.CreateTaskDTO;
import br.com.todoit.api.dto.tasks.UpdateTaskDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
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

    @Column(nullable = false)
    private Boolean completed = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Task(CreateTaskDTO data) {
        this.title = data.title();
        this.description = data.description();
        this.startAt = data.startAt();
        this.endAt = data.endAt();
        this.priority = data.priority();
        this.completed = data.completed();
    }

    public void setTitle(String title) throws Exception{
        if (title.length() > 50) {
            throw new Exception("O campo deve contér no máximo 50 caracteres");
        }
        this.title = title;
    }

    public void updateTask(UpdateTaskDTO data) {
        if (data.title() != null) {
            this.title = data.title();
        }
        if (data.description() != null) {
            this.description = data.description();
        }
        if (data.startAt() != null) {
            this.startAt = data.startAt();
        }
        if (data.endAt() != null) {
            this.endAt = data.endAt();
        }
        if (data.priority() != null) {
            this.priority = data.priority();
        }
        if (data.completed() != null) {
            this.completed = data.completed();
        }
    }
}
