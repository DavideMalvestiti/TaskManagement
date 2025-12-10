package com.example.task_management.dto.request;

import javax.validation.constraints.NotBlank;
import com.example.task_management.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequest {

    @NotBlank(message = "Title is required")
    @Schema(description = "Title of the task", example = "Finish homework")
    private String title;

    @Schema(description = "Optional detailed description of the task", example = "Math exercises page 24")
    private String description;

    @Schema(description = "Current status of the task", example = "PENDING",
    		allowableValues = {"PENDING", "IN_PROGRESS", "COMPLETED"})
    private TaskStatus status;
}
