package br.com.api.todolist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Entity(name = "tb_tasks")
public class Task {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, updatable = false, nullable = false)
    private UUID id;

    @Column(length = 50, nullable = false)
    private String title;

    private String description;

    private LocalDateTime startAt;
    private LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    public void setTitle(String title) throws Exception{
        if (title.length() > 50) {
            throw new Exception("O campo deve contér no máximo 50 caracteres");
        }
        this.title = title;
    }
}
