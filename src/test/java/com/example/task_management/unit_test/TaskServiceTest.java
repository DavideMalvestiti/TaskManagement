package com.example.task_management.unit_test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.example.task_management.dto.request.TaskRequest;
import com.example.task_management.dto.response.TaskResponse;
import com.example.task_management.factory.TaskFactory;
import com.example.task_management.model.Task;
import com.example.task_management.model.TaskStatus;
import com.example.task_management.repository.TaskRepository;
import com.example.task_management.service.TaskServiceImpl;


class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskFactory taskFactory;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private TaskResponse taskResponse;
    private TaskRequest taskRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        taskRequest = TaskRequest.builder()
                                 .title("Finish homework")
                                 .description("Math exercises page 24")
                                 .status(TaskStatus.PENDING)
                                 .build();

        task = Task.builder()
                   .id(1L)
                   .title("Finish homework")
                   .description("Math exercises page 24")
                   .status(TaskStatus.PENDING)
                   .createdAt(LocalDateTime.now())
                   .updatedAt(LocalDateTime.now())
                   .build();

        taskResponse = TaskResponse.builder()
                                   .id(1L)
                                   .title("Finish homework")
                                   .description("Math exercises page 24")
                                   .status(TaskStatus.PENDING)
                                   .createdAt(task.getCreatedAt())
                                   .updatedAt(task.getUpdatedAt())
                                   .build();
    }


    @Test
    void testGetByIdFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskFactory.toDto(task)).thenReturn(taskResponse);

        TaskResponse result = taskService.getById(1L);

        assertNotNull(result);
        assertEquals("Finish homework", result.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testGetByIdNotFound() {
        when(taskRepository.findById(2L)).thenReturn(Optional.empty());
        when(taskFactory.toDto(task)).thenReturn(taskResponse);

        assertThrows(NoSuchElementException.class, () -> taskService.getById(2L));
        verify(taskRepository, times(1)).findById(2L);
    }

    @Test
    void testCreate() {
        when(taskFactory.toEntity(taskRequest)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskFactory.toDto(task)).thenReturn(taskResponse);

        TaskResponse result = taskService.create(taskRequest);

        assertNotNull(result);
        assertEquals("Finish homework", result.getTitle());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testUpdate() {
        TaskRequest updateReq = TaskRequest.builder()
                                           .title("Updated Task")
                                           .description("Math exercises page 30")
                                           .status(TaskStatus.IN_PROGRESS)
                                           .build();

        Task updatedTask = Task.builder()
                               .id(1L)
                               .title("Updated Task")
                               .description("Math exercises page 30")
                               .status(TaskStatus.IN_PROGRESS)
                               .createdAt(task.getCreatedAt())
                               .updatedAt(task.getUpdatedAt())
                               .build();

        TaskResponse updatedResponse = TaskResponse.builder()
                                                   .id(1L)
                                                   .title("Updated Task")
                                                   .description("Math exercises page 30")
                                                   .status(TaskStatus.IN_PROGRESS)
                                                   .createdAt(task.getCreatedAt())
                                                   .updatedAt(task.getUpdatedAt())
                                                   .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
        when(taskFactory.toDto(updatedTask)).thenReturn(updatedResponse);

        TaskResponse result = taskService.update(1L, updateReq);

        assertEquals("Updated Task", result.getTitle());
        assertEquals(TaskStatus.IN_PROGRESS, result.getStatus());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testDeleteExists() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        taskService.delete(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotExists() {
        when(taskRepository.existsById(2L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> taskService.delete(2L));
    }

    @Test
    void testGetDtoListAll() {
        List<Task> tasks = Arrays.asList(task);
        when(taskRepository.findAll()).thenReturn(tasks);
        when(taskFactory.toDtoList(tasks)).thenReturn(Arrays.asList(taskResponse));

        List<TaskResponse> result = taskService.getDtoList(null);

        assertEquals(1, result.size());
        verify(taskRepository, times(1)).findAll();
    }

}
