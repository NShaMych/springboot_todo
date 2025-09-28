package com.springboot.todo.controller;

import com.springboot.todo.dto.DTO;
import com.springboot.todo.entity.Task.Status;
import com.springboot.todo.service.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlUtils.BASE_URL)
@RequiredArgsConstructor
public class ControllerImpl implements Controller {

    private final ServiceImpl service;

    //принимаем из тела запроса json и преобразуем в dto, затем возвращаем тот же dto
    @PostMapping
    @Override
    public DTO create(@RequestBody DTO dto) {
        return service.create(dto);
    }

    //принимаем из url не обязательные параметры статуса и сортировки, если их нет, возвращаем полный список задач
    @GetMapping
    @Override
    public List<DTO> getAll(@RequestParam(required = false) Status status,
                            @RequestParam(defaultValue = "dueDate") String sortBy) {
        return service.getAll(status, sortBy);
    }

    //берем из url конкретный id, и данные из json, которыми будем обновлять задачу
    @PutMapping(UrlUtils.ID)
    @Override
    public DTO update(@PathVariable Long id, @RequestBody DTO dto) {
        return service.update(id, dto);
    }

    //берем из url конкретный id, передаем в метод delete сервиса и удалям
    @DeleteMapping(UrlUtils.ID)
    @Override
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
