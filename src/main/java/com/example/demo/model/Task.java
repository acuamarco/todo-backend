package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.NonNull;
@Data
@AllArgsConstructor
@Entity
@Table(name = "task")
public class Task {
  private @Id @NonNull UUID id;
  private String title;
  private String description;
  private LocalDate dueDate;
  private int priority;
  private boolean completed;
  private LocalDate createdAt;
  private LocalDate updatedAt;

  public Task() {
    this.id = UUID.randomUUID();
    this.title = "New Task";
    this.completed = false;
    this.priority = 0;
    this.createdAt = LocalDate.now();
    this.updatedAt = LocalDate.now();
  }
}
