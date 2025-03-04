package br.com.api.todolist.repository;

import br.com.api.todolist.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

}
