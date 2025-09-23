package com.springboot.todo.service;

import com.springboot.todo.dto.DTO;
import com.springboot.todo.entity.Task;
import com.springboot.todo.entity.Task.Status;
import com.springboot.todo.repository.Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class Service {

    private final Repository repository;

    //Преобразуем задачу в объект DTO
    private DTO taskToDTO(Task task) {
        return DTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .status(task.getStatus())
                .build();
    }

    /*Создаем задачу(берем из dto через getter поля, через builder создаем task, сохраняем в репозиторий и затем
    преобразуем task обратно в dto и возвращаем
     */
    @Transactional
    public DTO create(DTO dto) {
        Task task = Task.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .dueDate(dto.getDueDate())
                .status(dto.getStatus())
                .build();
        repository.save(task);
        return taskToDTO(task);
    }

    //компоратор сортирует задачи по входному параметру либо по status либо по dueDate
    private Comparator<Task> getComparator(String sortBy) {
        if ("status".equalsIgnoreCase(sortBy)) {
            return (t1, t2) -> t1.getStatus().compareTo(t2.getStatus());
        } else {
            return (t1, t2) -> t1.getDueDate().compareTo(t2.getDueDate());
        }
    }

    @Transactional(readOnly = true)
    public List<DTO> getAll(Status status, String sortBy) {
        //если не нужны задачи с определенным статусом, возвращаем все задачи
        List<Task> tasks = (status != null) ? repository.findByStatus(status) : repository.findAll();

        return tasks.stream()
                .sorted(getComparator(sortBy))
                .map(task -> taskToDTO(task))
                .collect(Collectors.toList());
    }

    @Transactional
    public DTO update(Long id, DTO dto) {
        //ищем по id либо кидаем NoSuchElementException
        Task task = repository.findById(id).orElseThrow();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        task.setStatus(dto.getStatus());
        repository.save(task);
        return taskToDTO(task);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
