package com.springboot.todo.service;

import com.springboot.todo.dto.DTO;
import com.springboot.todo.entity.Task;
import com.springboot.todo.entity.Task.Status;
import com.springboot.todo.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceTest {

    @Mock
    private Repository repository;

    @InjectMocks
    private ServiceImpl service;

    private DTO testDto;

    @BeforeEach
    void setUp() {
        testDto = DTO.builder()
                .id(1L)
                .title("Test Task")
                .description("Test Description")
                .dueDate(LocalDate.now())
                .status(Status.TODO)
                .build();
    }

    @Test
    @DisplayName("Создание задачи: должен вернуть DTO после сохранения")
    void testCreate() {
        Task savedTask = Task.builder()
                .id(testDto.getId())
                .title(testDto.getTitle())
                .description(testDto.getDescription())
                .dueDate(testDto.getDueDate())
                .status(testDto.getStatus())
                .build();

        when(repository.save(any(Task.class))).thenReturn(savedTask);

        DTO result = service.create(testDto);

        assertNotNull(result);
        assertEquals(testDto.getTitle(), result.getTitle());
        verify(repository).save(any(Task.class));
    }

    @Test
    @DisplayName("Получение задач по статусу: сортировка по статусу")
    void testGetAll_withStatusAndSortByStatus() {
        Task task1 = Task.builder()
                .id(1L)
                .title("Task 1")
                .status(Status.TODO)
                .dueDate(LocalDate.now())
                .build();

        when(repository.findByStatus(Status.TODO)).thenReturn(List.of(task1));

        List<DTO> result = service.getAll(Status.TODO, "status");

        assertEquals(1, result.size());
        assertEquals(Status.TODO, result.get(0).getStatus());
        verify(repository).findByStatus(Status.TODO);
    }

    @Test
    @DisplayName("Получение всех задач: сортировка по дате выполнения")
    void testGetAll_withoutStatusAndSortByDueDate() {
        Task task1 = Task.builder()
                .id(1L)
                .title("A")
                .status(Status.TODO)
                .dueDate(LocalDate.now().plusDays(2))
                .build();

        Task task2 = Task.builder()
                .id(2L)
                .title("B")
                .status(Status.TODO)
                .dueDate(LocalDate.now().plusDays(1))
                .build();

        when(repository.findAll()).thenReturn(List.of(task1, task2));

        List<DTO> result = service.getAll(null, "dueDate");

        assertEquals(2, result.size());
        assertEquals("B", result.get(0).getTitle());
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Обновление существующей задачи: должны обновиться поля")
    void testUpdate_existingTask() {
        Long id = 1L;
        Task existingTask = Task.builder()
                .id(id)
                .title("Old Title")
                .description("Old Description")
                .dueDate(LocalDate.now())
                .status(Status.TODO)
                .build();

        DTO updateDto = DTO.builder()
                .id(id)
                .title("New Title")
                .description("New Description")
                .dueDate(LocalDate.now().plusDays(1))
                .status(Status.IN_PROGRESS)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(existingTask));
        when(repository.save(any(Task.class))).thenReturn(existingTask);

        DTO result = service.update(id, updateDto);

        assertEquals("New Title", result.getTitle());
        assertEquals(Status.IN_PROGRESS, result.getStatus());
        verify(repository).save(existingTask);
    }

    @Test
    @DisplayName("Обновление несуществующей задачи: должно выброситься исключение")
    void testUpdate_taskNotFound_shouldThrow() {
        Long id = 999L;
        DTO dto = DTO.builder().id(id).build();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.update(id, dto));
    }

    @Test
    @DisplayName("Удаление задачи по ID: должен вызваться deleteById")
    void testDelete() {
        Long id = 1L;
        doNothing().when(repository).deleteById(id);

        service.delete(id);

        verify(repository).deleteById(id);
    }
}
