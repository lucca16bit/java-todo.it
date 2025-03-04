package br.com.api.todolist.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Entity(name = "tb_users")
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String name;

    @Column(unique = true, nullable = false)  // para garantir que os valores de uma coluna sejam únicos no banco de dados
    private String login;

    @Column(nullable = false)
    private String password;
}
