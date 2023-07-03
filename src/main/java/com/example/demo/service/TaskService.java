package com.example.demo.service;

import com.example.demo.data.TaskAlreadyExistsException;
import com.example.demo.data.TaskNotFoundException;
import com.example.demo.model.Task;
import java.util.List;
import java.util.UUID;

public interface TaskService {
  Task createTask(Task task) throws TaskAlreadyExistsException;
  List<Task> getAllTasks();
  Task getTaskById(UUID taskId) throws TaskNotFoundException;
  Task updateTask(UUID taskId, Task task) throws TaskNotFoundException;
  void deleteTask(UUID taskId) throws TaskNotFoundException;
}
