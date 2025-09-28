package com.springboot.todo.service;

import com.springboot.todo.dto.DTO;
import com.springboot.todo.entity.Task;
import com.springboot.todo.entity.Task.Status;
import com.springboot.todo.repository.Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceImpl implements TaskService {

    private final Repository repository;

    /*Создаем задачу(берем из dto через getter поля, через builder создаем task, сохраняем в репозиторий и затем
    преобразуем task обратно в dto и возвращаем
     */
    @Transactional
    @Override
    public DTO create(DTO dto) {
        Task task = Task.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .dueDate(dto.getDueDate())
                .status(dto.getStatus())
                .build();
        Optional<Task> existing = repository.findByTitleAndDueDate(dto.getTitle(), dto.getDueDate());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Такая задача уже существует");
        }
        repository.save(task);
        DTO createDto = Mapper.taskToDTO(task);
        return createDto;

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
    @Override
    public List<DTO> getAll(Status status, String sortBy) {
        //если не нужны задачи с определенным статусом, возвращаем все задачи
        List<Task> tasks = (status != null) ? repository.findByStatus(status) : repository.findAll();
        List<DTO> tasksDTO = tasks.stream()
                .sorted(getComparator(sortBy))
                .map(task -> Mapper.taskToDTO(task))
                .collect(Collectors.toList());
        return tasksDTO;
    }

    @Transactional
    @Override
    public DTO update(Long id, DTO dto) {
        //ищем по id либо кидаем NoSuchElementException
        Task task = repository.findById(id).orElseThrow();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        task.setStatus(dto.getStatus());
        repository.save(task);
        DTO updateDto = Mapper.taskToDTO(task);
        return updateDto;
    }

    @Transactional
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
