package com.example.demo.controller;

import static com.example.demo.model.TaskTest.getTestTask;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.data.TaskAlreadyExistsException;
import com.example.demo.data.TaskNotFoundException;
import com.example.demo.model.Task;
import com.example.demo.service.TaskService;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class TaskControllerTest {

  @Mock
  private MockMvc mockMvc;

  @Mock
  private TaskService taskService;

  @InjectMocks
  private TaskController taskController;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
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
  void givenValidTaskId_whenDeleteTask_t6henReturnOk() throws TaskNotFoundException {
    var task = getTestTask();
    doNothing().when(taskService).deleteTask(task.getId());

    var result = taskController.deleteTask(task.getId());

    assertEquals(HttpStatus.OK, result.getStatusCode());
  }

  @Test
  void givenNonExistingTaskId_whenDeleteTask_thenReturnNotFound() throws TaskNotFoundException {
    var task = getTestTask();
    doThrow(new TaskNotFoundException("not found")).when(taskService).deleteTask(task.getId());

    var result = taskController.deleteTask(task.getId());

    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
  }

  @Test
  void givenGetRequestWithNoId_whenNoTaskExist_thenReturnAnEmptyList() throws Exception {
    this.mockMvc.perform(get("/api/v1/tasks/"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"))
        .andDo(MockMvcResultHandlers.print())
        .andReturn();
  }

  @Test
  void givenGetRequestWithNoId_whenTasksExist_thenReturnTaskList() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    var tasks = List.of(
        getTestTask(),
        getTestTask(),
        getTestTask()
    );
    var jsonTasks = objectMapper.writeValueAsString(tasks);
    when(taskService.getAllTasks()).thenReturn(tasks);

    this.mockMvc.perform(get("/api/v1/tasks/"))
        .andExpect(status().isOk())
        .andExpect(content().json(jsonTasks))
        .andDo(MockMvcResultHandlers.print())
        .andReturn();
  }
}
