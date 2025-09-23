package com.springboot.todo.repository;

import com.springboot.todo.entity.Task;
import com.springboot.todo.entity.Task.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Repository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(Status status);
}
