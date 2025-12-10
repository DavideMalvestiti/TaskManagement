package com.example.task_management.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.task_management.dto.request.TaskRequest;
import com.example.task_management.dto.response.TaskResponse;
import com.example.task_management.model.TaskStatus;
import com.example.task_management.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


/**
 * REST controller for managing tasks.
 * <p>
 * Provides endpoints to create, retrieve, update, delete, and list tasks.
 * All endpoints are secured with Basic Authentication.
 * </p>
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

	@Autowired
    private TaskService taskService;


	/**
     * Create a new task.
     *
     * @param req the task request containing title, description, and status
     * @return the created task with HTTP status 201
     */
    @Operation(summary = "Create a new task")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Task successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<TaskResponse> create(@Validated @RequestBody TaskRequest req) {
        TaskResponse res = taskService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    /**
     * Retrieve a task by its ID.
     *
     * @param id the ID of the task
     * @return the task found with HTTP status 200
     */
    @Operation(summary = "Get a task by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Task found"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getById(id));
    }

    /**
     * List all tasks, optionally filtering by status.
     *
     * @param status optional status filter
     * @return a list of tasks with HTTP status 200
     */
    @Operation(summary = "List tasks", description = "Optionally filter by status")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of tasks retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<TaskResponse>> tasksList(
            @Parameter(description = "Filter tasks by status")
            @RequestParam(required = false) TaskStatus status) {

        return ResponseEntity.ok(taskService.getDtoList(status));
    }

    /**
     * Update an existing task.
     *
     * @param id  the ID of the task to update
     * @param req the task request containing updated fields
     * @return the updated task with HTTP status 200
     */
    @Operation(summary = "Update single task")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Task updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(
            @PathVariable Long id,
            @Validated @RequestBody TaskRequest req) {

        return ResponseEntity.ok(taskService.update(id, req));
    }

    /**
     * Delete a task by its ID.
     *
     * @param id the ID of the task to delete
     * @return HTTP status 204 if deletion is successful
     */
    @Operation(summary = "Delete a task by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
    	taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
