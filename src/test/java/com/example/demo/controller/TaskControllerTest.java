package com.example.demo.controller;

import static com.example.demo.model.TaskTest.getTestTask;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.example.demo.data.TaskAlreadyExistsException;
import com.example.demo.data.TaskNotFoundException;
import com.example.demo.model.Task;
import com.example.demo.service.TaskService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

class TaskControllerTest {

  @Mock
  private TaskService taskService;

  @InjectMocks
  private TaskController taskController;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }
  @Test
  void givenValidTask_whenCreateTask_thenReturnCreated() throws TaskAlreadyExistsException {
    var task = getTestTask();
    when(taskService.createTask(any(Task.class))).thenReturn(task);

    var result = taskController.createTask(task);

    assertEquals(HttpStatus.CREATED, result.getStatusCode());
    assertEquals(task, result.getBody());
  }

  @Test
  void givenExistingTask_whenCreateTask_thenReturnConflict() throws TaskAlreadyExistsException {
    var task = getTestTask();
    when(taskService.createTask(any(Task.class))).thenThrow(new TaskAlreadyExistsException("already exists"));

    var result = taskController.createTask(task);

    assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
  }

  @Test
  void givenValidId_whenGetTask_thenReturnExistingTask() throws TaskNotFoundException {
    var task = getTestTask();
    when(taskService.getTaskById(task.getId())).thenReturn(task);

    var result = taskController.getTaskById(task.getId());

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals(task, result.getBody());
  }

  @Test
  void givenNonExistingId_whenGetTask_thenReturnNotFound() throws TaskNotFoundException {
    when(taskService.getTaskById(any())).thenThrow(new TaskNotFoundException("not found"));

    var result = taskController.getTaskById(UUID.randomUUID());

    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
  }

  @Test
  void givenTasksExist_whenGetTasks_thenReturnExistingTasks() {
    var tasks = List.of(
        getTestTask(),
        getTestTask(),
        getTestTask()
    );
    when(taskService.getAllTasks()).thenReturn(tasks);

    var result = taskService.getAllTasks();

    assertNotNull(result);
    assertEquals(result, tasks);
  }


  @Test
  void givenValidTask_whenUpdateTask_thenReturnOk() throws TaskNotFoundException {
    var task = getTestTask();
    when(taskService.updateTask(task.getId(), task)).thenReturn(task);

    var result = taskController.updateTask(task.getId(), task);

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals(task, result.getBody());
  }

  @Test
  void givenNonExistingTaskId_whenUpdateTask_thenReturnNotFound() throws TaskNotFoundException {
    var task = getTestTask();
    when(taskService.updateTask(task.getId(), task)).thenThrow(new TaskNotFoundException("not found"));

    var result = taskController.updateTask(task.getId(), task);

    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
  }

  @Test
  void givenValidTaskId_WhenDeleteTask_ThenReturnOk() throws TaskNotFoundException {
    var task = getTestTask();
    doNothing().when(taskService).deleteTask(task.getId());

    var result = taskController.deleteTask(task.getId());

    assertEquals(HttpStatus.OK, result.getStatusCode());
  }

  @Test
  void givenNonExistingTaskId_WhenDeleteTask_ThenReturnNotFound() throws TaskNotFoundException {
    var task = getTestTask();
    doThrow(new TaskNotFoundException("not found")).when(taskService).deleteTask(task.getId());

    var result = taskController.deleteTask(task.getId());

    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
  }
}
