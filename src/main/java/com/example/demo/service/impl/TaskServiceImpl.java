package com.example.demo.service.impl;

import com.example.demo.data.TaskAlreadyExistsException;
import com.example.demo.data.TaskNotFoundException;
import com.example.demo.data.TaskRepository;
import com.example.demo.model.Task;
import com.example.demo.service.TaskService;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {
  private final TaskRepository taskRepository;

  public TaskServiceImpl(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  @Override
  public Task createTask(Task task) throws TaskAlreadyExistsException {
    var existing = taskRepository.findById(task.getId());
    if (existing.isPresent()) {
      throw new TaskAlreadyExistsException("Task already exists ID:" + task.getId());
    }
    task.setCreatedAt(LocalDate.now());
    task.setUpdatedAt(LocalDate.now());
    return taskRepository.save(task);
  }

  @Override
  public List<Task> getAllTasks() {
    return taskRepository.findAll();
  }

  @Override
  public Task getTaskById(UUID taskId) throws TaskNotFoundException {
    return taskRepository.findById(taskId)
        .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + taskId));
  }

  @Override
  public Task updateTask(UUID taskId, Task task) throws TaskNotFoundException {
    var existingTask = getTaskById(taskId);
    existingTask.setTitle(task.getTitle());
    existingTask.setDescription(task.getDescription());
    existingTask.setDueDate(task.getDueDate());
    existingTask.setPriority(task.getPriority());
    existingTask.setCompleted(task.isCompleted());
    existingTask.setUpdatedAt(LocalDate.now());
    return taskRepository.save(existingTask);
  }

  @Override
  public void deleteTask(UUID taskId) throws TaskNotFoundException {
    var task = getTaskById(taskId);
    taskRepository.delete(task);
  }
}
