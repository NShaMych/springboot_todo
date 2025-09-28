package com.springboot.todo.controller;

import com.springboot.todo.dto.DTO;
import com.springboot.todo.entity.Task;
import org.springframework.web.bind.annotation.*;

import java.util.List;


public interface Controller {


    public DTO create(@RequestBody DTO dto);


    public List<DTO> getAll(@RequestParam(required = false) Task.Status status,
                            @RequestParam(defaultValue = "dueDate") String sortBy);


    public DTO update(@PathVariable Long id, @RequestBody DTO dto);


    public void delete(@PathVariable Long id);
}
