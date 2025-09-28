package com.springboot.todo.service;

import com.springboot.todo.dto.DTO;
import com.springboot.todo.entity.Task.Status;

import java.util.List;


public interface TaskService {


    public DTO create(DTO dto);

    public List<DTO> getAll(Status status, String sortBy);

    public DTO update(Long id, DTO dto);

    public void delete(Long id);
}
