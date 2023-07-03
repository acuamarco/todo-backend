package com.example.demo.data;

public class TaskAlreadyExistsException extends Exception {
  public TaskAlreadyExistsException(String message) {
    super(message);
  }
}
