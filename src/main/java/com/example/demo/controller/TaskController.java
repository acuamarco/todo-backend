package com.example.demo.controller;

import com.example.demo.data.TaskAlreadyExistsException;
import com.example.demo.data.TaskNotFoundException;
import com.example.demo.model.Task;
import com.example.demo.service.TaskService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {
  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @PostMapping
  public ResponseEntity<Task> createTask(@RequestBody Task task) {
    try {
      var createdTask = taskService.createTask(task);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    } catch (TaskAlreadyExistsException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

  @GetMapping("/")
  public ResponseEntity<List<Task>> getTasks() {
    return ResponseEntity.ok(taskService.getAllTasks());
  }

  @GetMapping("/{taskId}")
  public ResponseEntity<Task> getTaskById(@PathVariable UUID taskId) {
    try {
      var task = taskService.getTaskById(taskId);
      return ResponseEntity.ok(task);
    } catch (TaskNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @PutMapping("/{taskId}")
  public ResponseEntity<Task> updateTask(@PathVariable UUID taskId, @RequestBody Task task) {
    try {
      var updatedTask = taskService.updateTask(taskId, task);
      return ResponseEntity.ok(updatedTask);
    } catch (TaskNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @DeleteMapping("/{taskId}")
  public ResponseEntity<Void> deleteTask(@PathVariable UUID taskId) {
    try {
      taskService.deleteTask(taskId);
      return ResponseEntity.ok().build();
    } catch (TaskNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}
