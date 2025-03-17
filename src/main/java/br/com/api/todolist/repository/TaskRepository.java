package br.com.api.todolist.repository;

import br.com.api.todolist.entity.Task;
import br.com.api.todolist.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    Page<Task> findByUser(User user, Pageable pageable);
}
