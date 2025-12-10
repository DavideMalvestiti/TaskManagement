package com.example.task_management.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.example.task_management.dto.response.TaskResponse;
import com.example.task_management.dto.request.TaskRequest;
import com.example.task_management.factory.TaskFactory;
import com.example.task_management.model.Task;
import com.example.task_management.model.TaskStatus;
import com.example.task_management.repository.TaskRepository;


/**
 * Service implementation for managing {@link Task} entities.
 */
@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
    private TaskFactory taskFactory;

	@Autowired
    private TaskRepository taskRepository;


	/**
     * Retrieves a task by its ID.
     *
     * @param id the unique identifier of the task
     * @return {@link TaskResponse} DTO containing task data
     * @throws NoSuchElementException if no task exists with the given ID
     */
	@Override
    @Transactional(readOnly = true)
    public TaskResponse getById(Long id) {
        Task task = taskRepository.findById(id)
                                  .orElseThrow(() -> new NoSuchElementException("Task not found"));
        return taskFactory.toDto(task);
    }

	/**
     * Returns a list of tasks, optionally filtered by status.
     *
     * @param status optional filter for {@link TaskStatus};
     *               if null, all tasks are returned
     * @return list of {@link TaskResponse}
     */
	@Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getDtoList(TaskStatus status) {
    	List<Task> tasks;
        if (status != null) {
            tasks = taskRepository.findAllByStatus(status);
        } else {
            tasks = taskRepository.findAll();
        }
        return taskFactory.toDtoList(tasks);
    }

	/**
     * Creates a new task.
     *
     * @param request DTO containing the task's initial data
     * @return the newly created {@link TaskResponse}
     *
     * @throws IllegalArgumentException if the request contains invalid fields
     */
	@Override
    @Transactional(propagation = Propagation.REQUIRED)
    public TaskResponse create(TaskRequest request) {
    	Task task = taskFactory.toEntity(request);
    	Task saved = taskRepository.save(task);
        return taskFactory.toDto(saved);
    }

	/**
     * Updates an existing task by ID.
     *
     * <p>Only the fields present in the request are updated; null values are ignored.</p>
     *
     * @param id the ID of the existing task
     * @param request the DTO with updated fields
     * @return updated {@link TaskResponse}
     *
     * @throws NoSuchElementException if the task does not exist
     */
	@Override
    @Transactional(propagation = Propagation.REQUIRED)
    public TaskResponse update(Long id, TaskRequest request) {
        Task existing = taskRepository.findById(id)
                                      .orElseThrow(() -> new NoSuchElementException("Task not found"));

        existing.setTitle(request.getTitle());
        if (request.getDescription() != null) 
        	existing.setDescription(request.getDescription());
        if (request.getStatus() != null) 
        	existing.setStatus(request.getStatus());

        Task updated = taskRepository.save(existing);
        return taskFactory.toDto(updated);
    }

	/**
     * Deletes a task by ID.
     *
     * @param id ID of the task to delete
     *
     * @throws NoSuchElementException if the task does not exist
     */
	@Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new NoSuchElementException("Task not found");
        }
        taskRepository.deleteById(id);
    }

}
