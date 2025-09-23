package com.springboot.todo.dto;

import com.springboot.todo.entity.Task.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private Status status;
}
