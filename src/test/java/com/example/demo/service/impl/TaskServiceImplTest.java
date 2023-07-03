package com.example.demo.service.impl;

import static com.example.demo.model.TaskTest.getTestTask;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import static org.mockito.ArgumentMatchers.any;

import com.example.demo.data.TaskAlreadyExistsException;
import com.example.demo.data.TaskNotFoundException;
import com.example.demo.data.TaskRepository;
import com.example.demo.model.Task;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TaskServiceImplTest {

  @Mock
  private TaskRepository taskRepository;

  @InjectMocks
  private TaskServiceImpl taskService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }


  @Test
  void givenValidTask_whenCreateTask_thenReturnCreatedTask() throws TaskAlreadyExistsException {
    var task = getTestTask();
    when(taskRepository.save(any(Task.class))).thenReturn(task);

    var result = taskService.createTask(task);

    assertNotNull(result);
    assertEquals(task, result);
  }

  @Test
  void givenTasksExist_whenGetAllTasks_thenReturnExistingTasks() {
    var tasks = List.of(
        getTestTask(),
        getTestTask(),
        getTestTask()
    );
    when(taskRepository.findAll()).thenReturn(tasks);

    var result = taskService.getAllTasks();

    assertNotNull(result);
    assertEquals(result, tasks);
  }

  @Test
  void givenExistingTaskId_whenGetTaskById_thenReturnTask() throws TaskNotFoundException {
    var task = getTestTask();
    when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

    var result = taskService.getTaskById(task.getId());

    assertNotNull(result);
    assertEquals(task, result);
  }

  @Test
  void givenExistingTask_whenUpdateTask_thenReturnModifiedTask() throws TaskNotFoundException {
    var existingTask = getTestTask();
    var updatedTask = getTestTask(existingTask.getId());
    when(taskRepository.findById(existingTask.getId())).thenReturn(Optional.of(existingTask));
    when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

    var result = taskService.updateTask(existingTask.getId(), updatedTask);

    assertNotNull(result);
    assertEquals(updatedTask, result);
  }

  @Test
  void givenExistingTask_whenDeleteTask_thenTaskIsDeleted() throws TaskNotFoundException {
    var task = getTestTask();
    when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

    taskService.deleteTask(task.getId());

    verify(taskRepository, times(1)).findById(task.getId());
    verify(taskRepository, times(1)).delete(task);
  }
}
