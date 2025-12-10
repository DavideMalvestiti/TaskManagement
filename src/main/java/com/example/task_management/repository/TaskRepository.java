package com.example.task_management.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.task_management.model.Task;
import com.example.task_management.model.TaskStatus;


public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByStatus(TaskStatus status);
    
}
