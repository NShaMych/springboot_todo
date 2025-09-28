package com.springboot.todo.repository;

import com.springboot.todo.entity.Task;
import com.springboot.todo.entity.Task.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface Repository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(Status status);
    Optional<Task> findByTitleAndDueDate(String title, LocalDate dueDate);
}
