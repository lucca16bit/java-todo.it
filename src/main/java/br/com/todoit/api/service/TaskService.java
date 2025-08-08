package br.com.todoit.api.service;

import br.com.todoit.api.dto.services.DateRangeDTO;
import br.com.todoit.api.entity.Task;
import br.com.todoit.api.exception.InvalidDateException;
import br.com.todoit.api.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class TaskService {

    @Autowired
    private TaskRepository repository;

    public void validate(Task task) {
        if (task.getStartAt().isBefore(LocalDateTime.now())) {
            throw new InvalidDateException("A data de início e data de término deve ser maior do que a data atual.");
        }
        if (task.getStartAt().isAfter(task.getEndAt())) {
            throw  new InvalidDateException("A data de término deve ser maior do que a data de início");
        }
    }

    public DateRangeDTO getTodayDate() {
        LocalDate today = LocalDate.now();

        return new DateRangeDTO(
                today.atStartOfDay(),
                today.atTime(23, 59, 59)
        );
    }

    public DateRangeDTO getTomorrowDate() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        return new DateRangeDTO(
                tomorrow.atStartOfDay(),
                tomorrow.atTime(23, 59, 59)
        );
    }
}
