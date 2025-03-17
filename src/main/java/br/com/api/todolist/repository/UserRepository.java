package br.com.api.todolist.repository;

import br.com.api.todolist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    UserDetails findByLogin(String login);

    Optional<User> findById(UUID uuid);
}
