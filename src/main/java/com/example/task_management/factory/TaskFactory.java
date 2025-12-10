package com.example.task_management.factory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.example.task_management.dto.request.TaskRequest;
import com.example.task_management.dto.response.TaskResponse;
import com.example.task_management.model.Task;
import com.example.task_management.model.TaskStatus;


@Component
public class TaskFactory {

    public Task toEntity(TaskRequest request) {
        if (request == null) return null;

        return Task.builder()
                   .title(request.getTitle())
                   .description(request.getDescription())
                   .status(request.getStatus() != null ? request.getStatus() : TaskStatus.PENDING)
                   .build();
    }

    // for test only
    public Task toEntity(TaskResponse response) {
        if (response == null) return null;

        return Task.builder()
                   .id(response.getId())
                   .title(response.getTitle())
                   .description(response.getDescription())
                   .status(response.getStatus())
                   .createdAt(response.getCreatedAt())
                   .updatedAt(response.getUpdatedAt())
                   .build();
    }

    public TaskResponse toDto(Task task) {
        if (task == null) return null;

        return TaskResponse.builder()
                           .id(task.getId())
                           .title(task.getTitle())
                           .description(task.getDescription())
                           .status(task.getStatus())
                           .createdAt(task.getCreatedAt())
                           .updatedAt(task.getUpdatedAt())
                           .build();
    }

    public List<TaskResponse> toDtoList(List<Task> tasks) {
        if (tasks == null) return Collections.emptyList();
        return tasks.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
    }

}
