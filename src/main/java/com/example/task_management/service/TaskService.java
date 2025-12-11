package com.example.task_management.service;

import java.util.List;
import com.example.task_management.dto.request.TaskRequest;
import com.example.task_management.dto.response.TaskResponse;
import com.example.task_management.model.TaskStatus;


public interface TaskService {
	TaskResponse getById(Long id);
	List<TaskResponse> getDtoList(TaskStatus status, int page, int size);
	TaskResponse create(TaskRequest request);
	TaskResponse update(Long id, TaskRequest request);
	void delete(Long id);
}
