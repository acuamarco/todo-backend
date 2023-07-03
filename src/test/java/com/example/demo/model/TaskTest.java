package com.example.demo.model;

import java.time.LocalDate;
import java.util.UUID;

public class TaskTest {
  public static Task getTestTask(UUID id) {
    return new Task(id, "Task " + id, "Description", LocalDate.now(), 1, false, LocalDate.now(), LocalDate.now());
  }

  public static Task getTestTask() {
    return getTestTask(UUID.randomUUID());
  }
}
