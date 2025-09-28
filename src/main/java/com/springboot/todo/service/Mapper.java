package com.springboot.todo.service;


import com.springboot.todo.dto.DTO;
import com.springboot.todo.entity.Task;

public class Mapper {

    public static DTO taskToDTO(Task task) {
        return DTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .status(task.getStatus())
                .build();
    }

}
