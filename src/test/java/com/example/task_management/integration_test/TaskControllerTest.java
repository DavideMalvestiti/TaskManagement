package com.example.task_management.integration_test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.example.task_management.dto.request.TaskRequest;
import com.example.task_management.dto.response.TaskResponse;
import com.example.task_management.model.TaskStatus;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/tasks";
        headers = new HttpHeaders();
        headers.setBasicAuth("admin", "admin");
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    private TaskResponse createTestTask(String title, String description, TaskStatus status) {
        TaskRequest request = TaskRequest.builder()
                                         .title(title)
                                         .description(description)
                                         .status(status)
                                         .build();

        HttpEntity<TaskRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<TaskResponse> response = restTemplate.postForEntity(baseUrl, entity, TaskResponse.class);

        return response.getBody();
    }


    @Test
    void testCreateAndGetTask() {
        TaskResponse createdTask = createTestTask("Original Task", "Math exercises page 24", TaskStatus.PENDING);

        assertThat(createdTask).isNotNull();
        assertThat(createdTask.getTitle()).isEqualTo("Original Task");

        Long taskId = createdTask.getId();

        ResponseEntity<TaskResponse> getResponse = restTemplate.exchange(
                baseUrl + "/" + taskId,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                TaskResponse.class
        );

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        TaskResponse fetchedTask = getResponse.getBody();
        assertThat(fetchedTask).isNotNull();
        assertThat(fetchedTask.getId()).isEqualTo(taskId);
        assertThat(fetchedTask.getTitle()).isEqualTo("Original Task");
    }

    @Test
    void testUpdateTask() {
        TaskResponse createdTask = createTestTask("Original Task", "Math exercises page 24", TaskStatus.PENDING);
        Long taskId = createdTask.getId();

        TaskRequest updateReq = TaskRequest.builder()
                                           .title("Updated Task")
                                           .description("Math exercises page 30")
                                           .status(TaskStatus.IN_PROGRESS)
                                           .build();

        HttpEntity<TaskRequest> entity = new HttpEntity<>(updateReq, headers);

        ResponseEntity<TaskResponse> updateResponse = restTemplate.exchange(
                baseUrl + "/" + taskId,
                HttpMethod.PUT,
                entity,
                TaskResponse.class
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        TaskResponse updatedTask = updateResponse.getBody();
        assertThat(updatedTask.getTitle()).isEqualTo("Updated Task");
        assertThat(updatedTask.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    void testDeleteTask() {
        TaskResponse createdTask = createTestTask("Task to Delete", "Math exercises page 24", TaskStatus.PENDING);
        Long taskId = createdTask.getId();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl + "/" + taskId,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getResponse = restTemplate.exchange(
                baseUrl + "/" + taskId,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testListTasks() {
    	createTestTask("Task 1", "Math exercises page 24", TaskStatus.PENDING);

        ResponseEntity<TaskResponse[]> response = restTemplate.exchange(
        		baseUrl + "?page=0&size=10",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                TaskResponse[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        TaskResponse[] tasks = response.getBody();
        assertThat(tasks).isNotNull();
        assertThat(tasks.length).isGreaterThanOrEqualTo(1);
    }

    @Test
    void testListTasksWithStatusFilter() {
    	createTestTask("Task 1", "Math exercises page 24", TaskStatus.PENDING);
    	createTestTask("Task 2", "Math exercises page 120", TaskStatus.IN_PROGRESS);

        ResponseEntity<TaskResponse[]> response = restTemplate.exchange(
                baseUrl + "?status=PENDING&page=0&size=10",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                TaskResponse[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        TaskResponse[] tasks = response.getBody();
        assertThat(tasks).isNotNull();
        assertThat(tasks.length).isGreaterThanOrEqualTo(1);

        for (TaskResponse t : tasks) {
            assertThat(t.getStatus()).isEqualTo(TaskStatus.PENDING);
        }
    }

    @Test
    void testCreateTaskValidationError() {
        TaskRequest invalidRequest = TaskRequest.builder()
                                                .title("")
                                                .description("Math exercises page 24")
                                                .status(TaskStatus.PENDING)
                                                .build();

        HttpEntity<TaskRequest> entity = new HttpEntity<>(invalidRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Title is required");
    }

    @Test
    void testGetTaskNotFound() {
        Long nonExistentId = 9999L;

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/" + nonExistentId,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Task not found");
    }

}
