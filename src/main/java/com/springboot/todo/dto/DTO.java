package com.springboot.todo.dto;

import com.springboot.todo.entity.Task.Status;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "{dto.id.notnull}")
    private Long id;
    @NotNull(message = "{dto.title.notnull}")
    private String title;
    private String description;
    private LocalDate dueDate;
    private Status status;
}
